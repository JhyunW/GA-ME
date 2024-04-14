import { create } from 'zustand';
import { log } from '../url/api';

interface TagDto {
  codeId:string
  tagId:number
  tagName:string
}

// 카트 아이탬 타입스크립트 선언식 tagsAll추가
export interface CartItem {
  gameId: number;
  imageUrl: string;
  title: string;
  price: string;
  developer: string;
  tagsAll?: TagDto[] | null;
  likes: number;
  isPrefer: boolean;
}

// 추가, 제거, 비우기 함수
interface PoketStore {
  cartItems: CartItem[];
  addItem: (userId: number | undefined, newItem: CartItem) => void;
  // updateItem:(userId: number | undefined, gameId: number) => void;
  removeItem: (userId: number | undefined, gameId: number) => void;
  clearCart: (userId: number | undefined) => void;
  
}

// 스토어 생성
const usePoketStore = create<PoketStore>((set) => ({
  cartItems: JSON.parse(sessionStorage.getItem('cartItems') || '[]'),

  // 같은 게임 아이디의 게임은 추가하지 않고, 최대 5개까지만 허용
  addItem: (userId, newItem) => set((state) => {
    const exists = state.cartItems.some(item => item.gameId === newItem.gameId);
    if (!exists && state.cartItems.length < 5) { // 여기에 제한 조건 추가
      const newCartItems = [...state.cartItems, newItem];
      sessionStorage.setItem('cartItems', JSON.stringify(newCartItems));

      // 사용자 패턴 로그
      log(userId, 'pocket', 'click', [
        { 'clicked_item': 'pocket_add' },
        { 'game_id': newItem.gameId},
      ]);

      return { cartItems: newCartItems };
    }
    return state;
  }),

  // 장바구니 게임 빼기
  removeItem: (userId, gameId) => set((state) => {
    const newCartItems = state.cartItems.filter(item => item.gameId !== gameId);
    sessionStorage.setItem('cartItems', JSON.stringify(newCartItems));
    
    // 사용자 패턴 로그
    log(userId, 'pocket', 'click', [
      { 'clicked_item': 'pocket_remove' },
      { 'game_id': gameId},
    ]);

    return { cartItems: newCartItems };
  }),

  // updateItem: (userId, gameId) => (state: { cartItems: CartItem[]; }) => {
  //   const updateItems:CartItem[] = state.cartItems.filter((item: CartItem) => item.gameId == gameId);
  //   const item:CartItem = updateItems[0];
  //   if (item.isPrefer) {
  //     item.likes = (item.likes != null ? item.likes - 1 : 0);
  //     item.isPrefer = false;
  //   } else {
  //     item.likes = (item.likes != null ? item.likes + 1 : 0);
  //     item.isPrefer = true;
  //   }
    
  //   // 사용자 패턴 로그
  //   log(userId, 'pocket', 'click', [
  //     { 'clicked_item': 'pocket_remove' },
  //     { 'game_id': gameId},
  //   ]);
  // },
  // 장바구니 초기화
  clearCart: (userId) => set(() => {
    sessionStorage.removeItem('cartItems');

    // 사용자 패턴 로그
    log(userId, 'pocket', 'click', [
      { 'clicked_item': 'pocket_clear_all' },
    ]);

    return { cartItems: [] };
  }),
}));

export default usePoketStore;