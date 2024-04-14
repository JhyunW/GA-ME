import { create } from "zustand";

interface currentPage {
    nowCategory: string;
    selectedTagName: string | null;
    setNowCategory: (category: string) => void;
    setSelectedTagName: (tagName: string | null) => void;
}

const useStoreCurrentPage = create<currentPage>((set) => ({
    nowCategory: '전체 인기', // 카테고리 기본값
    selectedTagName: null, // 태그 기본값

    setNowCategory: (category) => set({ nowCategory: category }),
    setSelectedTagName: (tagName) => set({ selectedTagName: tagName }),
}));

export default useStoreCurrentPage;