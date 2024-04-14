// src/url/api.tsx
import axios from 'axios';

// 환경변수에서 카카오 클라이언트 ID와 리디렉션 URI를 가져옵니다.
// 카카오에서는 REST API 키를 클라이언트 ID로 사용합니다.
const KAKAO_CLIENT_ID = import.meta.env.VITE_KAKAO_CLIENT_ID as string;
const KAKAO_REDIRECT_URI = import.meta.env.VITE_KAKAO_REDIRECT_URI as string;

const BASE_URL = "https://j10e105.p.ssafy.io/api";

const api = axios.create({
  baseURL: BASE_URL, // 기본 api 주소
  headers: {
    "Content-Type": `application/json;charset=UTF-8`,
    "Accept": "application/json",
    "Access-Control-Allow-Origin": 'https://j10e105.p.ssafy.io',
    'Access-Control-Allow-Credentials':"true",
  }
});

// 사용자를 카카오 로그인 페이지로 리디렉트하는 함수
export const redirectToKakaoOAuth = () => {
  // 카카오 OAuth 로그인 URL 생성
  const oauthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${encodeURIComponent(KAKAO_CLIENT_ID)}&redirect_uri=${encodeURIComponent(KAKAO_REDIRECT_URI)}&response_type=code`;
  // 현재 브라우저를 해당 URL로 리디렉트
  window.location.href = oauthUrl;
};

// 서버에 인증 코드를 전송하여 사용자 정보를 받아오는 함수
export const fetchKakaoUserInfo = async (code: string) => {
  console.log("code: "+code);
  try {
    // 인증 코드를 서버에 전송
    const response = await api.post('/auth/kakao/callback', { code });
    // 서버로부터 받은 사용자 정보 처리
    console.log(response.data);
    return response.data; // 사용자 정보 반환
  } catch (error) {
    console.error('Failed to fetch user information', error);
    throw new Error('Failed to fetch user information');
  }
};

// 얏호~! 사용자 행동패턴 로그 남기는 함수!
export const log = async (userId: number | undefined, page: string, action: string, args: Array<object>) => {
  try {
    const response = await api.post(`/tracking/log`, {
      userId: userId, // 유저 아이디
      page: page, // 페이지
      action: action, // 사용자 행동
      args: args, // 파라미터
    });
    console.log(response);
  } catch (error) {
    throw new Error('Failed to log');
  }
}


// 게임명 검색 API 함수
export const searchGames = async (keyword: string, userId: number) => {
  try {
    const response = await api.post('/search', {
      keyword,
      userId
    });
    // 사용자 패턴 로그
    log(userId, 'search', 'click', [
      { 'clicked_item': 'search' },
      { 'search_keyword': keyword},
    ]);
    return response.data; // 검색 결과를 반환
  } catch (error) {
    throw new Error('Failed to search games');
  }
};

export { api };
