import { create } from 'zustand';
import axios, { AxiosError } from 'axios';
import { ChoiceGame } from '../components/SurveyComponents/SurveyGame';
import { api } from '../url/api';

// API 응답 데이터의 타입을 정의합니다.
interface ApiResponse {
    isSuccess: boolean;
    message: string;
    code: number;
    result: ChoiceGame[]; // `any` 대신 더 구체적인 타입을 사용해주세요.
}

interface SurveyStoreState{
    data: ApiResponse | null;
    loading: boolean;
    error: AxiosError | null;
    backGroundImg: string | null;
    checkGameList: number[][];

    setBackgroundImg: (gameImg: string) => void;
    addCheckChoiceGame: (gameId: number, current: number) => void;
    removeCheckChoiceGame: (gameId: number, current: number) => void;
    fetchData: () => Promise<void>;
}

export const surveyStore = create<SurveyStoreState>((set) => ({
    data: null,
    loading: false,
    error: null,
    backGroundImg: null,
    checkGameList: [[], [], []],    

    setBackgroundImg(gameImg: string){
      set({backGroundImg: gameImg});
    },

    addCheckChoiceGame(gameId: number, current: number) {
        // state라는 파라미터가 이전 값을 기억
        set((state) => {
          const updatedCheckGameList = [...state.checkGameList]; // 이전 상태를 복사하여 수정할 배열 생성
          updatedCheckGameList[current] = [...updatedCheckGameList[current], gameId]; // 현재 그룹에 선택된 게임 추가
          return { ...state, checkGameList: updatedCheckGameList }; // 새로운 상태 반환
        });
      },
    
      removeCheckChoiceGame(gameId: number, current: number) {
        // state라는 파라미터가 이전 값을 기억
        set(state => {
          const updatedCheckGameList = [...state.checkGameList]; // 이전 상태를 복사하여 수정할 배열 생성
          updatedCheckGameList[current] = updatedCheckGameList[current].filter(id => id !== gameId); // 해당 그룹에서 선택된 게임 제거
          return { ...state, checkGameList: updatedCheckGameList }; // 새로운 상태 반환
        });
      },

    fetchData: async() => {
        set({ loading: true });
        try {
            const response = await api.get<ApiResponse>(`/users/choice-game`);
            set({ data: response.data, loading: false });          
        } catch (error) {
            if (axios.isAxiosError(error)) {
                set({ error, loading: false });
            }
        }
    }
}));

