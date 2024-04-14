// 장현욱

import { useState, useEffect } from "react";
import { NavLink, useLocation, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import Swal from "sweetalert2";
import style from "./Navbar.module.css";
import useUserStore from "../../stores/userStore";

// 네비게이션 링크를 위한 타입 정의
interface NavLinkItem {
  path?: string;
  label: string;
  icon: string;
  activeIcon: string;
  action?: () => void; // 클릭 시 실행할 액션(함수) 추가
}

const Navbar: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const { isLoggedIn, setIsLoggedIn } = useUserStore();  // 유저정보 스토어
  const isActive = (path: string) => location.pathname === path;  // 현재 위치 확인
  const fetchAndSetUser = useUserStore((state) => state.fetchAndSetUser);
  const setUser = useUserStore((state) => state.setUser);
  const { user } = useUserStore();

  const navLinkYPositions: number[] = [0, 58, 118, 183, 238]; // 각 네비게이션 항목에 대한 Y 위치

  // 로컬 스토리지에서 indicatorY 상태를 읽어오기
  const initialY: number = localStorage.getItem("indicatorY")
    ? Number(localStorage.getItem("indicatorY"))
    : navLinkYPositions[0];
  const [indicatorY, setIndicatorY] = useState<number>(initialY);

  // 로그인 함수
  const handleLoginClick = () => {
    window.Kakao.Auth.login({
      success: async function (authObj) {
        // console.log(authObj); // 인증 정보 출력
        // authObj 객체에서 access_token을 추출
        const accessToken = authObj.access_token;
        // 스토어의 fetchAndSetUser 함수 호출하여 서버에 사용자 정보 요청
        const isNewUser = await fetchAndSetUser(accessToken); // 비동기 처리에 await 추가

        if (isNewUser == true) {
          navigate('/survey'); // 신규 사용자인 경우 설문조사 페이지로 이동
        }

        setIsLoggedIn(true);
      },
      fail: function (err) {
        console.error(err); // 에러 처리
      },
    });
  };

  // 쿠키 삭제
  const deleteCookie = (name: string) => {
    document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:01 GMT;";
  };

  // 로그아웃 함수
  const handleLogoutClick = () => {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    if (window.Kakao.Auth.getAccessToken()) {
      Swal.fire({
        title: "로그아웃 하시겠습니까?",
        text: "로그아웃 하면 가중치가 쌓이지 않아요!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "로그아웃",
        cancelButtonText: "취소",
      }).then((result) => {
        if (result.isConfirmed) {
          // eslint-disable-next-line @typescript-eslint/ban-ts-comment
          // @ts-expect-error
          window.Kakao.Auth.logout(() => {
            console.log("카카오 로그아웃 완료");
            // 여기에서 인증 관련 쿠키 삭제
            deleteCookie("auth_token");
            // 상태 업데이트
            setIsLoggedIn(false);
            setUser(null);
            // 페이지 새로고침
            window.location.reload();
          });
          Swal.fire({
            title: "로그아웃 되었습니다!",
            icon: "success"
          });
        }
      });
    }
  };

  // const getMyPage = () => {
  //   if(user) navigate(`/myPage/${user.userId}`)
  // } 

  // 각 네비게이션 정의
  const navLinks: NavLinkItem[] = [
    {
      path: "/",
      label: "Main",
      icon: "/Gameicon.png",
      activeIcon: "/Gameicon.gif",
    },
    {
      path: "/search",
      label: "Search",
      icon: "/SearchIcon.png",
      activeIcon: "/SearchIcon.gif",
    },
    {
      path: "/mixAndMatch",
      label: "Mix Match",
      icon: "/MixAndMatch.png",
      activeIcon: "/MixAndMatch.gif",
    },
    {
      path: "/topic",
      label: "Hot Topic",
      icon: "/FireIcon.png",
      activeIcon: "/FireIcon.gif",
    },

    // 로그인 상태에 따라 분기 처리
    !user
      ? {
        label: "Login",
        icon: "/ProfileIcon.png",
        activeIcon: "/ProfileIcon.gif",
        action: handleLoginClick,
      } :
      {
        path: "/myPage",
        label: "My Page",
        icon: "/ProfileIcon.png",
        activeIcon: "/ProfileIcon.gif",
      },
    // isLoggedIn이 true일 때만 로그아웃 버튼 객체를 배열에 추가
    ...(isLoggedIn
      ? [
        {
          label: "Logout",
          icon: "/Logout.png",
          activeIcon: "/Logout.gif",
          action: handleLogoutClick,
        },
      ]
      : []),
  ];

  useEffect(() => {
    // path가 정의된 링크만 찾기
    const activeLinkIndex = navLinks.findIndex(
      (link) => link.path && isActive(link.path)
    );
    if (activeLinkIndex !== -1) {
      const newY = navLinkYPositions[activeLinkIndex];
      setIndicatorY(newY);
      // 위치 상태를 로컬 스토리지에 저장 ( 현재 위치를 보여주기 위함 )
      localStorage.setItem("indicatorY", newY.toString());
    }
  }, [location.pathname]); // location.pathname이 변경될 때마다 실행

  const variants = {
    active: { y: indicatorY },
  };

  return (
    <>
      {/* Nav박스 */}
      <div
        className={`${style.neonBorder} fixed top-0 left-5 flex flex-col items-center px-8 h-screen py-20 border-r-2 bg-gray-900 text-white z-40`}
      >

        {/* 네브 로고 */}
        <div className="mb-24">
          <NavLink to="/">
            <motion.img
              className="w-24"
              src="/GGAME.gif"
              alt="GGAMELOGO"
              whileHover={{ y: -20 }}
              transition={{ type: "spring", stiffness: 300, damping: 20 }}
            />
          </NavLink>
        </div>

        {/* 현재 페이지 위치를 알려줄 상태표시기 */}
        <div className="relative pl-8 pr-7">
          <motion.div
            className={`absolute left-0 px-2 py-1 border-2 rounded-full ${style.neonBorder2}`}
            style={{ width: "calc(110% - 1rem)", maxWidth: "300px", height: "2.8rem" }}
            variants={variants}
            initial={false}
            animate="active"
          >
            <p className={`${style.arrow}`}>▷</p>
          </motion.div>

          {navLinks.map((link, index) => {
            // path가 있는 경우 NavLink 컴포넌트 사용
            if (link.path) {
              return (
                <NavLink
                  key={index}
                  to={link.path}
                  className={`${style.navBox} flex items-center space-x-2 mb-8 ml-2`}
                >
                  <img
                    src={isActive(link.path) ? link.activeIcon : link.icon}
                    className="transition-all duration-300 ease-in-out"
                    style={{
                      width: "28px",
                      height: "auto",
                      filter: "brightness(0) invert(1)",
                    }}
                    alt={`${link.label} Icon`}
                  />
                  <span className={`${style.labelColor} text-xl`}>
                    {link.label}
                  </span>
                </NavLink>
              );
            }
            else {
              // path가 없는 경우 (예: 로그인 버튼), div와 onClick 이벤트를 사용
              return (
                <div
                  key={index}
                  onClick={link.action}
                  className="flex items-center space-x-2 mb-8 cursor-pointer ml-2"
                >
                  <img
                    src={link.icon}
                    className="transition-all duration-300 ease-in-out"
                    style={{
                      width: "28px",
                      height: "auto",
                      filter: "brightness(0) invert(1)",
                    }}
                    alt={`${link.label} Icon`}
                  />
                  <span className={`${style.neonNormal} text-xl`}>
                    {link.label}
                  </span>
                </div>
              );
            }
          })}
        </div>
      </div>
    </>
  );
};

export default Navbar;
