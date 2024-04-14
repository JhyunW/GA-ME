import { create } from "zustand";
import { api } from '../url/api';
import axios, { AxiosError } from "axios";
import { log } from "../url/api";
import noUsedTag from '../components/MyPageComponents/NoUsedTagList'

// interface로 response data들에 대한 타입을 미리 지정해줌
// dto랑 비슷한 느낌으로 사용
// result 내부에 원하는 정보에 존재함
interface ApiResponse {
    isSuccess: boolean;
    message: string;
    code: number;
    result: UserInfo;
}
// 유저의 기본 정보와 선호 게임이 존재
interface UserInfo {
    userId: number;
    userName: string;
    userProfileImg: string;
    userAge: number;
    preferList: Prefer[];
    tagWeightList: TagWeight[];
}
// 선호하는 게임에 대한 정보가 존재
interface Prefer {
    gameId: number;
    gameName: string;
    gameHeaderImg: string;
    gamePriceInitial: number;
    gamePriceFinal: number;
    gameDeveloper: string;
    gameLike: number;
    isPrefer: boolean;
    tagList: TagList[];
}
// 선호하는 게임에 대한 태그 정보들이 존재

interface TagList {
    codeId: string;
    tagId: number;
    tagName: string;
}
// 유저의 태그에 대한 정보가 존재
interface TagWeight {
    userId: number;
    tagId: number;
    codeId: string;
    tagName: string;
    userTagWeight: number;
}
// zustand store에서 사용하기 위해서 미리 정보들을 초기화
const initialTagList: TagList = {
    codeId: '', tagId: 0, tagName: ''
}

const initialPrefer: Prefer = {
    gameId: 0,
    gameName: '',
    gameHeaderImg: '',
    gamePriceInitial: 0,
    gamePriceFinal: 0,
    gameDeveloper: '',
    gameLike: 0,
    isPrefer: true,
    tagList: [initialTagList]
}

const initialTagWeight: TagWeight = {
    userId: 0,
    tagId: 0,
    codeId: '',
    tagName: '',
    userTagWeight: 0
}

const initialUser: UserInfo = {
    userId: 0,
    userName: '',
    userProfileImg: '',
    userAge: 0,
    preferList: [initialPrefer],
    tagWeightList: [initialTagWeight]

}

const initialData: ApiResponse = {
    isSuccess: false,
    message: '',
    code: 0,
    result: initialUser
}

interface myPageDetail {
    data: ApiResponse;
    loading: boolean;
    error: AxiosError | null;
    topTenTag: TagWeight[];
    setData: (resData: ApiResponse) => void;
    fetchData: (userId: number) => void;
    addLikeWeight: (userId: number, gameList: number[][]) => void;
}

const myPageStore = create<myPageDetail>((set) => ({
    data: initialData,
    loading: false,
    error: null,
    topTenTag: [],

    setData: (resData: ApiResponse) => set({ data: resData }),
    fetchData: async (userId: number) => {
        try {
            const response = await api.get(`/users/${userId}`);
            set({ data: response.data, loading: false });
            set(state => {
                const topTenTag: TagWeight[] = [];
                state.data.result.tagWeightList.forEach((tag: TagWeight) => {           
                    if (!noUsedTag.some(item => item.tagId === tag.tagId && item.codeId === tag.codeId)) {
                        topTenTag.push(tag);
                    }
                    
                });
                return { ...state, topTenTag };
            })
        }
        catch (error) {
            console.error(error);
            if (axios.isAxiosError(error)) {
                set({ error, loading: false });
            }
        }

    },
    addLikeWeight: async (userId: number, gameList: number[][]) => {
      const action: string = "like";
      const page: string = "survey";
      gameList.map(async (arrayGame: number[]) => {
        try {
          arrayGame.map(async (gameId: number) => {
            await api.put(
              `/tracking?userId=${userId}&gameId=${gameId}&action=${action}`
            );
            // 사용자 패턴 로그
            log(userId, page, action, [
              { 'favorite': gameId }, // 디테일
            ]);
          });
        } catch (error) {
          console.error(error);
        }
      });
    },
}))

// eslint-disable-next-line react-refresh/only-export-components
export { myPageStore };
export type { ApiResponse, TagWeight, UserInfo, Prefer, TagList };
