import { create } from "zustand";
import axios, { AxiosError } from "axios";
import { log } from "../url/api";

// API 응답 데이터의 타입을 정의합니다.
interface ApiResponse {
  isSuccess: boolean;
  message: string;
  code: number;
  result: []; // `any` 대신 더 구체적인 타입을 사용해주세요.
}

interface SaleApiResponse {
  isSuccess: boolean;
  message: string;
  code: number;
  result: SaleGameDto[]; // `any` 대신 더 구체적인 타입을 사용해주세요.
}
// SaleGameDto 타입 정의
interface SaleGameDto {
  salePercent: number;
  cardDtoList: CardDto[];
}
// CardDto 타입 정의
interface CardDto {
  gameId: number;
  gameName: string;
  gameHeaderImg: string;
  gamePriceInitial: number;
  gamePriceFinal: number;
  gameDeveloper: string;
  gameDiscountPercent: number;
  gameLike: number;
  isPrefer: boolean;
  tagList?: Array<{
    codeId: string;
    tagId: number; // 이제 tagId가 필수입니다.
    tagName: string;
  }> | null;
}

// 스토어 상태의 타입을 정의합니다.
interface StoreState {
  newsData: ApiResponse | null;
  saleData: SaleApiResponse | null;
  saleData10: CardDto[] | null;
  saleData30: CardDto[] | null;
  saleData50: CardDto[] | null;
  saleData75: CardDto[] | null;
  nLoading: boolean;
  sLoading: boolean;
  nError: AxiosError | null;
  sError: AxiosError | null;
  newsFetched: boolean;
  setNewsFetched: (fetched: boolean) => void;
  fetchNewsData: (userId: number) => Promise<void>;
  fetchSalesData: (userId: number) => Promise<void>;
}

const api = axios.create({
  baseURL: "https://j10e105.p.ssafy.io",
  headers: {
    "Content-Type": `application/json;charset=UTF-8`,
    Accept: "application/json",
    "Access-Control-Allow-Origin": `http://localhost:5173/`,
    "Access-Control-Allow-Credentials": "true",
  },
});


const useHotTopicStore = create<StoreState>((set, get) => ({
  newsData: null,
  saleData: null,
  saleData10: null,
  saleData30: null,
  saleData50: null,
  saleData75: null,
  nLoading: false,
  sLoading: false,
  nError: null,
  sError: null,
  newsFetched: false, // 뉴스 데이터가 성공적으로 가져와졌는지 여부
  
  setNewsFetched: (fetched: boolean) => set({ newsFetched: fetched }),

  fetchNewsData: async (userId:number) => {
    if (get().nLoading) return; // 이미 로딩 중이면 요청하지 않음
  
    set({ nLoading: true });
    const maxRetries = 5; // 최대 재시도 횟수
    let retries = 0;
  
    while (retries < maxRetries) {
      const postData = { userId };
  
      try {
        const response = await api.post<ApiResponse>("/api/topics/news", postData);
        if (response.data.isSuccess) {
          set({ newsData: response.data, nLoading: false, newsFetched: true }); // 성공 시 newsFetched도 업데이트
          return; // 성공한 경우 함수 종료
        } else {
          // API 요청이 실패하더라도 서버에서 isSuccess가 false를 반환할 경우에만 재시도합니다.
          retries++;
          console.log(`Retrying request... Attempt ${retries}`);
        }
      } catch (error) {
        if (axios.isAxiosError(error)) {
          console.error("Error fetching news data:", error); // 오류를 콘솔에 기록
          set({ nError: error });
          break; // 오류 발생 시 재시도 중단
        }
      }
    }
    set({ nLoading: false }); // 모든 시도 후 로딩 상태 종료, 성공/실패 관계 없이 실행되어야 함
  },
  
  fetchSalesData: async (userId:number) => {
    const postData = { userId };
    set({ sLoading: true });
    try {
      const response = await api.post<SaleApiResponse>(
        `/api/topics/discount`,
        postData
      );
      response.data.result.forEach((saleItem) => {
        switch (saleItem.salePercent) {
          case 10:
            set({ saleData10: saleItem.cardDtoList });
            break;
          case 30:
            set({ saleData30: saleItem.cardDtoList });
            break;
          case 50:
            set({ saleData50: saleItem.cardDtoList });
            break;
          case 75:
            set({ saleData75: saleItem.cardDtoList });
            break;
          default:
            break;
        }
        // 사용자 패턴 로그
        log(userId, "hot_topic", "click", [
            { clicked_item: "sale_percent" },
            { percent: saleItem.salePercent },
          ]);
      });
      set({ saleData: response.data, sLoading: false });
    } catch (error) {
      if (axios.isAxiosError(error)) {
        console.error("Error fetching sales data:", error); // 오류를 콘솔에 기록
        set({ sError: error, sLoading: false });
      }
    }
  },
}));

export default useHotTopicStore;
