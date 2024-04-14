import create from "zustand";
import { api } from "../url/api.tsx";

// 사용 스토어의 구조를 기반으로 하는 구조
export interface Game {
  gameId: number;
  gameName: string;
  gameHeaderImg: string;
  gamePriceFinal: number;
  // 각 태그를 기준으로 각 태그 및 이름을 가진 경우 선언방법
  tagList: Array<{ codeId: string; tagId: number; tagName: string }>;
  gameLike: number;
  isPrefer: boolean;
  gameDeveloper: string;
  gamePriceInitial: number;
}

export interface Screenshot {
  path_full: string;
  path_thumbnail: string;
  id: number;
}

export interface GameData {
  gameId: number;
  gameName: string;
  gameShortDescription: string;
  gameDetailedDescription: string;
  gameHeaderImg: string;
  gameWebsite: string;
  gameDeveloper: string;
  gamePublisher: string;
  gamePriceInitial: number;
  gamePriceFinal: number | 0; // gameCard에 맞춰서 string
  gameDiscountPercent: number;
  gameReleaseDate: string;
  screenshotList: Screenshot[];
  relatedGameList: Game[]; // 관련 게임 데이터에 대한 정확한 타입이 없는 경우 any로 지정
  gameTagList:
    | Array<{ codeId: string; tagId: number; tagName: string }>
    | undefined;
  gameIsLike: boolean;
}

export interface statisticsDto {
  gameId: number;
  statisticsBaseAt: string;
  timeValues: number[];
  positiveCounts: number[];
  negativeCounts: number[];
}

export interface statistics {
  isSuccess: null;
  gameWordCloudUrl: string |null; // 04/02 수정
  statisticsDto: statisticsDto;
}

interface ApiResponse {
  isSuccess: boolean;
  message: string;
  code: number;
  result: GameData;
}

interface ApiResponsestatistics {
  isSuccess: boolean;
  message: string;
  code: number;
  result: statistics;
}

type DetailState = {
  data: ApiResponse | null;
  statisticsResult: ApiResponsestatistics | null;

  fetchData: (
    userId: number | undefined,
    gameId: number | undefined
  ) => Promise<void>;
  toggleIsLike: (
    gameIsLike: boolean | undefined,
    gameId: number | undefined,
    userId: number | undefined
  ) => Promise<void>;
  statisticsData: (gameId: number | undefined) => Promise<void>;
};

interface ApiResponse {
  isSuccess: boolean;
  message: string;
  code: number;
}

export const useDetailStore = create<DetailState>((set) => ({
  data: null,
  statisticsResult: null,
  fetchData: async (gameId, userId) => {
    // 데이터 가져오는 비동기 요청
    try {
      const response = await api.get<ApiResponse>(
        `/games/${gameId}/info/${userId}`
      );
      set({ data: response.data });
      console.log(response.data);
    } catch (error) {
      console.log(error);
    }
  },

  toggleIsLike: async (gameIsLike, gameId, userId) => {
    try {
      let response;
      if (gameIsLike) {
        response = await api.delete<ApiResponse>(`/game/prefer`, {
          data: { userId, gameId },
        });
      } else {
        response = await api.post<ApiResponse>(`/game/prefer`, {
          userId,
          gameId,
        });
      }

      // 요청이 성공하고 응답 코드가 100인 경우에만 gameIsLike 값을 토글합니다.
      if (response && response.data.code === 100) {
        set((state) => ({
          ...state,
          data: {
            ...state.data!,
            result: {
              ...state.data!.result,
              gameIsLike: !gameIsLike,
            },
          },
        }));
      } else {
        console.error("응답 코드가 100이 아닙니다.");
      }
    } catch (error) {
      console.error("요청이 실패했습니다.", error);
    }
  },

  statisticsData: async (gameId) => {
    // 데이터 가져오는 비동기 요청
    try {
      const response = await api.get<ApiResponsestatistics>(
        `/games/${gameId}/statistics`
      );
      set({ statisticsResult: response.data });
      console.log(response.data);
    } catch (error) {
      console.log(error);
    }
  },
}));
