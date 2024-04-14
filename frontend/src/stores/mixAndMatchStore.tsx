import { create } from "zustand";
import axios from "axios";

const api = axios.create({
  baseURL: "https://j10e105.p.ssafy.io",
  headers: {
    "Content-Type": `application/json;charset=UTF-8`,
    Accept: "application/json",
    // 추가
    "Access-Control-Allow-Origin": `http://localhost:5173/`,
    "Access-Control-Allow-Credentials": "true",
  },
});

interface RequestData {
  userId?: number; 
  gameIdAndTagDtoList: {
    gameId: number;
    tagList: TagDto[] | null | undefined;
  }[];
}

interface TagDto {
  codeId: string;
  tagId: number;
  tagName: string;
}

interface gameCardDto {
  gameId: number;
  gameName: string;
  gameHeaderImg: string;
  gamePriceInitial: number;
  gamePriceFinal: number;
  gameDeveloper: string;
  gameLike: number;
  isPrefer: boolean;
  tagList: TagDto[];
}

interface SearchResult {
  tagDtoList: TagDto[],
  gameCardDtoList: gameCardDto[]
}

interface SearchState {
  results: SearchResult | null; // 검색 결과를 저장할 상태 // 검색 결과를 저장할 상태
  loading: boolean;
  setResults: (results: SearchResult) => void; // 검색 결과를 업데이트하는 액션
  setLoading: (loading: boolean) => void; // 로딩 상태를 업데이트하는 액션
  fetchData: (postData: RequestData) => void;
}

const useMixAndMatchStore = create<SearchState>((set) => ({
  results: null, // 초기 상태는 빈 배열
  loading: false, // 초기 로딩 상태는 false
  setResults: (results) => set({ results }), // 검색 결과 업데이트
  setLoading: (loading) => set({ loading }), // 로딩 상태 업데이트

  fetchData: async (postData: RequestData) => {
    try {
      set({ loading: true }); // 로딩 시작
      const response = await api.post(`/api/recommendations/search`, postData);
      // Zustand 스토어에 응답 데이터를 저장합니다.
      set({results:response.data.result})
      return response.data;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        console.log(error);
      }
    } finally {
      set({ loading: false }); // 로딩 종료
    }
  },
}));

export default useMixAndMatchStore;
