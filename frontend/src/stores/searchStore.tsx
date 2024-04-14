// src/stores/searchStore.tsx
import create from 'zustand';

interface Tag {
  codeId: string;
  tagId: number;
  tagName: string;
}

interface SearchResult {
  gameId: number,
  gameName: string,
  gameHeaderImg: string,
  gamePriceInitial: number,
  gamePriceFinal: number,
  gameDeveloper: string,
  gameLike: null,
  isPrefer: false,
  tagList: Tag[]
}

interface SearchState {
  results: SearchResult[]; // 검색 결과를 저장할 상태
  isLoading: boolean;
  searchPerformed: boolean; // 검색이 수행되었는지 여부
  setResults: (results: SearchResult[]) => void; // 검색 결과를 업데이트하는 액션
  setIsLoading: (loading: boolean) => void;
  setSearchPerformed: (performed: boolean) => void; // 검색 수행 여부를 업데이트하는 액션
}

const useSearchStore = create<SearchState>((set) => ({
  results: [],
  isLoading: false,
  searchPerformed: false, // 초기 상태는 false로 설정
  setResults: (results) => set({ results }),
  setIsLoading: (isLoading) => set({ isLoading }),
  setSearchPerformed: (searchPerformed) => set({ searchPerformed }), // 상태 업데이트 함수
}));

export default useSearchStore;
