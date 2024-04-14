// 담장자 : 장현욱


import { motion } from "framer-motion";
import React, { useState, useEffect } from "react";
import usePoketStore from "../../stores/poketStore";
import useStoreLike from "../../stores/likeStore";
import useUserStore from "../../stores/userStore";
import Swal from "sweetalert2";
import style from "./GameCard.module.css";
import { useDrag } from 'react-dnd';

// TagDtop 타입스크립트 정의
interface TagDto {
  codeId: string;
  tagId: number;
  tagName: string;
}

// 타입스크립트 타입 프롭받을 타입 정의
export interface GameCardProps {
  gameId: number;
  imageUrl: string;
  title: string;
  developer: string;
  price: string;
  beforPrice: string;
  tagsAll?: TagDto[] | null;
  tags: string[];
  likes: number | null;
  isPrefer: boolean;
  onGameClick: (gameId: number) => void; // 해당 게임 디테일로 이둉
}

// 타입스크립트식 선언
const GameCard: React.FC<GameCardProps> = ({
  gameId,
  imageUrl,
  title,
  developer,
  price,
  beforPrice,
  tagsAll,
  tags,
  likes,
  isPrefer,
  onGameClick,
}) => {
  const [isHovered, setIsHovered] = useState(false); // 호버 상태
  const { cartItems, addItem, removeItem } = usePoketStore(); // 포켓스토어
  const [showAlert, setShowAlert] = useState(false); // 경고 메시지 상태 추가
  const { likeGame, unlikeGame, setGameId, setUserId, disLike } = useStoreLike(); // Like스토어
  const { user, isLoggedIn } = useUserStore(); // 유저정보 스토어

  // 좋아요 상태와 갯수를 즉작적으로 보여주기 위한 변수
  const [isLiked, setIsLiked] = useState(isPrefer);
  const [likeCount, setLikeCount] = useState(likes);


  // 카트 안에 데이터가 있는지 확인
  const isInCart = cartItems.some((item) => item.gameId === gameId);

  // 유저정보 확인
  useEffect(() => {
    // 정보가 있으면 유저ID 없으면 0 으로 세팅
    setUserId(user?.userId ?? 0);
  }, [user]);

  // 드래그 앤 드롭(드래그 가능 타입으로 지정)
  const [{ isDragging }, dragRef] = useDrag(() => ({
    type: 'GAME_CARD', // 드래그 아이템 타입을 지정합니다.
    item: { gameId, imageUrl, title, price, tagsAll, developer, likes, isPrefer }, // 드래그하는 항목의 데이터를 지정합니다.
    collect: (monitor) => ({
      isDragging: !!monitor.isDragging(), // 현재 드래그 상태를 반영합니다.
    }),
  }));

  // 포켓에 넣는 핸들러
  const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.stopPropagation();
    if (isInCart) {
      removeItem(user?.userId, gameId);
    } else {
      if (cartItems.length >= 5) {
        setShowAlert(true);
        setTimeout(() => setShowAlert(false), 3000);
        return;
      }
      addItem(user?.userId, {
        gameId,
        imageUrl,
        title,
        price,
        tagsAll,
        developer,
        likes: likes ?? 0,
        isPrefer
      });
    }
  };

  // 좋아요와 좋아요 취소 핸들러
  const handleLikeToggle = async (
    event: React.MouseEvent<HTMLButtonElement>
  ) => {
    event.stopPropagation(); // 이벤트 버블링 중지
    if (user && isLoggedIn) {
      // user가 존재하는지 확인, 로그인 되어있는지 확인
      setUserId(user.userId); // user.userId를 스토어에 설정
      setGameId(gameId); // 게임 ID를 스토어에 설정
      if (isLiked) {
        await unlikeGame(); // 좋아요 취소 요청
        setIsLiked(false);
        setLikeCount((prevCount) => (prevCount ? prevCount - 1 : 0)); // Ensure not going below 0
      } else {
        await likeGame(); // 좋아요 요청
        setIsLiked(true);
        setLikeCount((prevCount) => (prevCount ? prevCount + 1 : 1)); // Initialize to 1 if null
      }
    } else {
      Swal.fire({
        icon: "error",
        title: "로그인 후 이용 가능합니다!",
        text: "왼쪽 아래 Login 버튼을 통해 로그인해주세요.",
      });
    }
  };

  // 관심없음 핸들러
  const handleDisLikeClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.stopPropagation();
    if (user && isLoggedIn) {
      // user가 존재하는지 확인, 로그인 되어있는지 확인
      disLike(tagsAll);
      Swal.fire({
        icon: "success",
        title: "가중치를 감소시켰습니다.",
        text: "가중치가 감소되면 해당 태그의 게임들이 노출되는 횟수를 줄여줘요.",
      });
    } else {
      Swal.fire({
        icon: "error",
        title: "로그인 후 이용 가능합니다!",
        text: "왼쪽 아래 Login 버튼을 통해 로그인해주세요.",
      });
    }
  };

  // 가격 ,변환을 위한 함수
  function formatPrice(priceStr: string) {
    const numericPrice = parseInt(priceStr?.substring(1), 10);
    return `₩${numericPrice.toLocaleString()}`;
  }

  // 카드 호버효과를 위한 변수
  const hoverEffects = {
    scale: 1.1,
    transition: { duration: 0.3 },
  };

  const overlayVariants = {
    hidden: { opacity: 0, backdropFilter: "none" },
    visible: { opacity: 1, backdropFilter: "blur(5px)" },
  };

  return (
    <>
      {/* 게임카드 컨테이너 */}
      <motion.div
        ref={dragRef} // 드래그 참조를 컴포넌트에 연결합니다.
        className={`${style.card} w-50 m-2 rounded overflow-hidden text-white text-center relative cursor-pointer`}
        whileHover={hoverEffects}
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
        onClick={() => onGameClick(gameId)}
        style={{ opacity: isDragging ? 0.5 : 1 }} // 드래그 중일 때 투명도를 변경합니다.
      >
        {/* 게임카드 내부 컨테이너 */}
        <div
          className={`${style.card} w-50 h-48 rounded overflow-hidden text-white text-left`}
        >
          <img src={imageUrl} alt={title} className="w-full h-" />
          <div className="p-2 mt-1">
            <h3
              className={`text-md leading-none font-sejong ${style.truncateLines}`}
            >
              {title}
            </h3>
          </div>

          {/* 세일정보에 따른 가격표시&개발사 : 카드의 하단 */}
          {price !== beforPrice ? (
            <div className="absolute bottom-0 text-xs font-thin p-2 font-Pretendard">
              <span className="text-gray-500 line-through">
                {formatPrice(beforPrice)}
              </span>
              {" => "}
              {formatPrice(price)}
              <p>
                {developer.length > 20
                  ? `${developer.substring(0, 20)}...`
                  : developer}
              </p>
            </div>
          ) : (
            <div className="absolute bottom-0 text-xs p-2 font-Pretendard">
              {formatPrice(price)}
              <p>
                {developer.length > 20
                  ? `${developer.substring(0, 20)}...`
                  : developer}
              </p>
            </div>
          )}
        </div>

        {/* 카드 내부 기능 아이콘 정의 */}
        {isHovered && (
          <motion.div
            className="absolute inset-0 bg-black bg-opacity-50 flex flex-col justify-between p-8"
            variants={overlayVariants}
            initial="hidden"
            animate="visible"
            exit="hidden"
          >
            <div className="flex justify-center items-center space-x-2">
              {/* 좋아요 버튼 */}
              <motion.button
                className="rounded-full p-2"
                onClick={handleLikeToggle}
                whileHover={{ scale: 1.2, rotate: 360 }}
                whileTap={{
                  scale: 0.8,
                  // rotate: -90,
                  borderRadius: "100%",
                }}
              >
                <img
                  src={isLiked ? "/OnLike.png" : "/Like.png"}
                  alt={"Like"}
                  className="w-6 h-6"
                  title="좋아요"
                />
              </motion.button>

              {/* 포켓에담기 버튼 */}
              <motion.button
                className={`rounded-full`} // 갈색 배경 클래스 조건부 적용
                onClick={handleClick}
                whileHover={{ scale: 1.2, rotate: 360 }}
                whileTap={{
                  scale: 0.8,
                  borderRadius: "100%",
                }}
              >
                <img
                  src={isInCart ? "/OnCart.png" : "/Cart.png"}
                  alt="Cart"
                  className="w-6 h-6"
                  title="포켓 담기"
                />
              </motion.button>

              {/* 관심없음 버튼 */}
              <motion.button
                className="rounded-full p-2"
                onClick={handleDisLikeClick}
                whileHover={{ scale: 1.2, rotate: 360 }}
                whileTap={{
                  scale: 0.8,
                  // rotate: -90,
                  borderRadius: "100%",
                }}
              >
                <img src={"/NotLike.png"} alt={"NotLike"} className="w-6 h-6" title="관심 없음"/>
              </motion.button>
            </div>
            {/* Tag 리스트 컨테이너 */}
            <div className="flex justify-center items-center">
              <div>
                {tags
                  .filter((tag: string) => tag.length <= 4)
                  .map((tag: string, index: number) => (
                    <span
                      key={index}
                      className="bg-black bg-opacity-50 rounded px-2 py-1 text-xs font-sejong mx-1 inline-block"
                    >{`#${tag}`}</span>
                  ))}
              </div>
            </div>
            <div className={`flex justify-center items-center mb-2`}>
              {/* 좋아요 수 */}
              <span
                className={`${style.neonNormal}`}
              >{`♥ : ${likeCount}`}</span>
            </div>
          </motion.div>
        )}
      </motion.div>
      {/* 카트에 5개 이상 담을시 alert */}
      {showAlert && (
        <div
          className={`fixed top-10 left-1/2 transform -translate-x-1/2 z-50 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded`}
          role="alert"
        >
          <strong className="font-bold">
            GA:ME포켓에는 최대 5개까지만 담을수 있어요!
          </strong>
          <span className="block sm:inline"></span>
          <span className="absolute top-0 bottom-0 right-0 px-4 py-3"></span>
        </div>
      )}
    </>
  );
};

export default GameCard;
