import { create } from 'zustand';
import axios, { AxiosError } from 'axios';

// API 응답 데이터의 타입을 정의합니다.
interface ApiTags {
    codeId: string,
    tagId: number,
    tagName: string
}

interface ApiResult {
    gameId: number,
    gameName: string,
    gameHeaderImg: string,
    gamePriceInitial: number,
    gamePriceFinal: number,
    gameDeveloper: string,
    gameLike: null,
    isPrefer: false,
    tagList: ApiTags[]
}

interface UserApiResult {
    tagDtoList: ApiTags[];
    gameCardDtoList: ApiResult[];
}

interface ApiResponse {
    isSuccess: boolean;
    message: string;
    code: number;
    result: ApiResult[]; // `any` 대신 더 구체적인 타입을 사용해주세요.
}

interface userApiResponse {
    isSuccess: boolean;
    message: string;
    code: number;
    result: UserApiResult; // `any` 대신 더 구체적인 타입을 사용해주세요.
}


// 스토어 상태의 타입을 정의합니다.
interface StoreState {
    data: ApiResponse | null;
    userGameData: userApiResponse | null
    bannerData: ApiResponse | null;
    loading: boolean;
    error: AxiosError | null;
    userId: number;
    codeId: string;
    tagId: number;
    page: number;
    size: number;
    setUserId: (userId: number) => void;
    setCodeId: (codeId: string) => void;
    setTagId: (tagId: number) => void;
    setPage: (page: number) => void;
    setSize: (size: number) => void;
    fetchMainData: () => Promise<void>;
    fetchUserGameData: () => Promise<void>;
    mainBanner: () => Promise<void>;
}

const api = axios.create({
    baseURL: 'https://j10e105.p.ssafy.io',
    headers: {
        "Content-Type": `application/json;charset=UTF-8`,
        "Accept": "application/json",
        // 추가  
        "Access-Control-Allow-Origin": `http://localhost:5173/`,
        'Access-Control-Allow-Credentials': "true",
    }
});

const useStoreMain = create<StoreState>((set, get) => ({
    data: null,
    userGameData: null,
    bannerData: null,
    loading: false,
    error: null,

    userId: 0,
    setUserId: (userId: number) => set({ userId }),

    codeId: '0',
    setCodeId: (codeId: string) => set({ codeId }),

    tagId: 0,
    setTagId: (tagId: number) => set({ tagId }),

    page: 1,
    setPage: (page: number) => set({ page }),

    size: 50,
    setSize: (size: number) => set({ size }),

    fetchMainData: async () => {
        const { userId, codeId, tagId, page, size } = get();        
        console.log('유저 코드 태그');
        
        

        set({ loading: true });
        try {
            const response = await api.get<ApiResponse>(`/api/recommendations/popular?userId=${userId}&codeId=${codeId}&tagId=${tagId}&page=${page}&size=${size}`);
            console.log(response.data);
            // 기존 데이터에 새로운 데이터를 추가하는 로직
            set({ data: response.data, loading: false });
            console.log(response.data);
        } catch (error) {
            if (axios.isAxiosError(error)) {
                set({ error, loading: false });
            }
        }
    },

    fetchUserGameData: async() => {
    const { userId, codeId, tagId } = get();
    console.log(userId);
    console.log(codeId);
    console.log(tagId);
    

    set({ loading: true });
    try {
        const response = await api.get<userApiResponse>(`/api/recommendations/personal/${userId}?codeId=${codeId}&tagId=${tagId}`);
        // 해당 유저의 추천 게임을 요청
        set({ userGameData: response.data, loading: false });
        console.log(response.data);
    } catch (error) {
        if (axios.isAxiosError(error)) {
            set({ error, loading: false });
        }
    }
},

mainBanner: async () => {
    set({ loading: true });
    try {
        const response = await api.get<ApiResponse>(`/api/recommendations/recent-popular`,);
        set({ bannerData: response.data, loading: false });
        console.log(response.data);
    } catch (error) {
        if (axios.isAxiosError(error)) {
            set({ error, loading: false });
        }
    }
},
}));

export default useStoreMain;