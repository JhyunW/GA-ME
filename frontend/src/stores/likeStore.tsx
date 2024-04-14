import { create } from "zustand";
import axios, { AxiosError } from "axios";
import { log } from "../url/api";

const api = axios.create({
  baseURL: "https://j10e105.p.ssafy.io/api",
  headers: {
    "Content-Type": `application/json;charset=UTF-8`,
    Accept: "application/json",
    // 추가
    "Access-Control-Allow-Origin": `http://localhost:5173/`,
    "Access-Control-Allow-Credentials": "true",
  },
});

interface TagDto {
  codeId: string;
  tagId: number;
  tagName: string;
}

interface StoreState {
  loading: boolean;
  error: AxiosError | null;
  userId: number;
  gameId: number;
  setUserId: (userId: number) => void;
  setGameId: (gameId: number) => void;
  likeGame: () => Promise<void>;
  unlikeGame: () => Promise<void>;
  disLike: (tagsAll: TagDto[] | null | undefined) => Promise<void>;
}

const useStoreLike = create<StoreState>((set, get) => ({
  loading: false,
  error: null,
  userId: 0,
  gameId: 0,
  setUserId: (userId: number) => set({ userId }),
  setGameId: (gameId: number) => set({ gameId }),

  likeGame: async () => {
    const { userId, gameId } = get();
    set({ loading: true });
    try {
      const response = await api.post(`/game/prefer`, { userId, gameId });
      // 요청 성공 시 데이터 업데이트
      set({ loading: false });
      console.log("Like successful", response.data);
      // 가중치 증가  
      api.put('/tracking?userId='+userId+'&gameId='+gameId+'&action=like');
      // 사용자 패턴 로그
      log(userId, "like", "like", [{ game_id: gameId }]);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        set({ error, loading: false });
      }
    }
  },

  unlikeGame: async () => {
    const { userId, gameId } = get();
    set({ loading: true });
    try {
      // 데이터를 지워서 한번 해보자..
      const response = await api.delete(`/game/prefer`, {
        data: { userId, gameId },
      });
      // 요청 성공 시 데이터 업데이트
      set({ loading: false });
      console.log("Unlike successful", response.data);
      // 가중치 감소  
      api.put('/tracking?userId='+userId+'&gameId='+gameId+'&action=unlike');
      // 사용자 패턴 로그
      log(userId, "like", "unlike", [
        { game_id: gameId },
      ]);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        set({ error, loading: false });
      }
    }
  },

  disLike: async (tagsAll) => {
    const { userId } = get();
    set({ loading: true });

    // tagsAll에서 필요한 정보만 추출하여 새 배열 생성
    const tags =
      tagsAll?.map((tag) => ({
        codeId: tag.codeId,
        tagId: tag.tagId,
      })) ?? []; // tagsAll이 null이나 undefined일 경우 빈 배열을 대신 사용

    try {
      const response = await api.put(`/tracking/dislike`, { userId, tags });
      set({ loading: false });
      console.log(response.data);

      // 사용자 패턴 로그
      log(userId, "like", "dislike", [
        { tags: tags},
      ]);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        set({ error, loading: false });
      }
    }
  },
}));

export default useStoreLike;
