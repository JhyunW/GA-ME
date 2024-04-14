import './App.css';
import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import { RouterProvider, createBrowserRouter,} from 'react-router-dom';
import Main from './pages/Main';
import Search from './pages/Search';
import Detail from './pages/Detail';
import MyPage from './pages/MyPage';
import Topic from './pages/HotTopic';
import MixAndMatch from './pages/MixAndMatch'
import Survey from './pages/Survey';
import { useEffect } from 'react';


const KAKAO_CLIENT_ID = import.meta.env.VITE_KAKAO_CLIENT_ID as string;


const router = createBrowserRouter([
  // 메인
  { path: '/', element: <Main />,},
  // 검색 페이지
  { path: 'search', element: <Search /> },
  // 믹스&매치 페이지
  { path: 'mixAndMatch', element: <MixAndMatch /> },
  // 게임 디테일 페이지
  { path: 'detail/:gameId', element: <Detail/> },
  // 마이페이지
  { path: 'myPage',element: <MyPage/>},
  // 토픽 페이지
  { path: 'topic', element: <Topic/>},
  // 선호도 조사 페이지
  { path: 'survey', element: <Survey/> }, // 날짜,차량,시간,위치,짐종류 같은거 제출
]);

export const PRIMARY_COLOR = '#4A3AFF';

// 사용자 프로필 정보 타입
interface UserProfile {
  profile_image: string;
  nickname: string;
  email: string;
}

// 카카오 로그인 응답 타입
interface AuthResponse {
  access_token: string;
  token_type: string;
  refresh_token: string;
  expires_in: number;
  scope: string;
  refresh_token_expires_in: number;
  user: UserProfile; // 사용자 정보를 여기에 포함
}

// window 객체에 대한 Kakao 타입 확장
declare global {
  interface Window {
    Kakao: {
      init: (appKey: string) => void;
      isInitialized: () => boolean;
      Auth: {
        login: (params: {
          success: (response: AuthResponse) => void;
          fail: (error: unknown) => void;
        }) => void;
      };
    };
  }
}

const App: React.FC = () => {
  useEffect(() => {
    // 카카오 SDK 초기화
    if (window.Kakao && !window.Kakao.isInitialized()) {
      window.Kakao.init(KAKAO_CLIENT_ID);
    }
  }, []);

  return (
    <DndProvider backend={HTML5Backend}>
    <RouterProvider router={router} />
    </DndProvider>
  );
};

export default App;