import styles from "./DetailBanner.module.css"; // CSS 모듈 import
import usePoketStore, { CartItem } from "../../stores/poketStore";
import React, { useState, useEffect } from "react";
import Swal from "sweetalert2";

// Define an interface for the props
interface BannerProps {
  bannerImage: string;
  gameId: number;
  gameName: string;
  gameShortDescription: string | undefined;
  gameIsLike: boolean | undefined;
  price: string;
  developer: string;
  tagsAll:
    | Array<{ codeId: string; tagId: number; tagName: string }>
    | undefined;
  likes: number;
  isPrefer: boolean;
}

import { useDetailStore } from "../../stores/DetailStore";
import useUserStore from "../../stores/userStore";
import { api, log } from "../../url/api";
// import OnLikeImage from '/OnLike.png';

const Banner: React.FC<BannerProps> = ({
  bannerImage,
  gameId,
  gameName,
  gameShortDescription,
  gameIsLike,
  price,
  developer,
  tagsAll,
  likes,
}) => {
  const [isLike, setIsLike] = useState<boolean>(gameIsLike ?? false);

  // 줄넘김이 적용된 텍스트
  const MAX_LENGTH = 40; // 최대 길이 지정
  const { toggleIsLike } = useDetailStore();
  const { user } = useUserStore();
  // 텍스트 길이가 이 값 이상이면 공백을 찾아서 줄넘김을 추가하는 함수
  // 문장이 끝날 때까지 단어 단위로 자르고, 각 줄의 길이를 체크하여 줄넘김을 추가하는 함수
  const addLineBreaks = (text: string, maxLength: number) => {
    const words = text.split(" ");
    let result = "";
    let line = "";
    for (const word of words) {
      let remainingWord = word;
      while (remainingWord.length > maxLength) {
        // 남은 단어가 maxLength보다 길면 maxLength 단위로 쪼개서 추가
        result += remainingWord.slice(0, maxLength) + "<br>";
        remainingWord = remainingWord.slice(maxLength);
      }
      // maxLength를 넘지 않는 단어를 추가
      line += remainingWord + " ";
      if (line.trim().length >= maxLength) {
        result += line.trim() + "<br>";
        line = "";
      }
    }
    result += line.trim(); // 마지막 줄 추가
    return result;
  };
  const modifiedShortDescription = addLineBreaks(
    gameShortDescription || "",
    MAX_LENGTH
  );

  const likeClickHandler = () => {
    if (user?.userId) {
      // user가 존재하는지 확인, 로그인 되어있는지 확인
      toggleIsLike(gameIsLike, gameId, user?.userId);

      //가중치 증가(좋아요)
      api.put(
        "/tracking?userId=" + user.userId + "&gameId=" + gameId + "&action=like"
      );

      // 사용자 패턴 로그
      log(user?.userId, "detail", "like", [{ game_id: gameId }]);
    } else {
      Swal.fire({
        icon: "error",
        title: "로그인 후 이용 가능합니다!",
        text: "왼쪽 아래 Login 버튼을 통해 로그인해주세요.",
      });
    }
  };
  // 버튼 클릭 핸들러 - 스팀으로 이동
  const steamButtonClickHandler = () => {
    if (user) {
      //가중치 증가(스팀 이동)
      api.put(
        "/tracking?userId=" +
          user.userId +
          "&gameId=" +
          gameId +
          "&action=go-steam"
      );

      //사용자 패턴 로그
      log(user?.userId, "detail", "move", [
        { move_page: "go-steam" },
        { game_id: gameId },
      ]);
    }

    const steamUrl = `https://store.steampowered.com/app/${gameId}/?l=koreana`;
    window.open(steamUrl, "_blank");
  };
  const { addItem } = usePoketStore();

  useEffect(() => {
    setIsLike(gameIsLike || false);
  }, [gameIsLike]);

  const handleAddToCart = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.stopPropagation(); // 이벤트 버블링 중지
    const imageUrl = bannerImage;
    const title = gameName;
    const isPrefer = gameIsLike ?? false;
    console.log(likes);
    price = price.slice(0, price.length - 2);
    const itemToAdd: CartItem = {
      gameId,
      imageUrl,
      title,
      price,
      developer,
      tagsAll,
      likes,
      isPrefer,
    };
    console.log(itemToAdd);
    addItem(user?.userId, itemToAdd);
  };

  return (
    <>
      <div className={styles.bannerContainer}>
        <div
          style={{ backgroundImage: `url(${bannerImage})` }}
          className={styles.bannerBackground}
        >
          {/* bg 이미지 */}
          <div className={styles.darkFilter}></div>

          {/* 내부 컨텐츠 */}
          <div
            style={{ backgroundImage: `url(${bannerImage})` }}
            className={styles.innerContent}
          >
            {/* 찐 이미지 */}
            {/* <img src={bannerImage} alt="Banner" className={styles.centerImage} /> */}

            {/* 좋아요 버튼 */}
            <button className={styles.likeButton} onClick={likeClickHandler}>
              <img
                className={styles.likeImg}
                src={isLike ? "/OnLike.png" : "/Like.png"}
                alt="Like"
              />
            </button>

            {/* 왼쪽 하단 텍스트 */}
            <div className={styles.leftBottomText}>
              <h1 className="font-taebaek">{gameName}</h1>
              <div
                className="font-sejong"
                dangerouslySetInnerHTML={{ __html: modifiedShortDescription }}
              />
            </div>
            {/* 오른쪽 하단 버튼 */}
            <div className={styles.rightBottomButtons}>
              <button
                className={`${styles.urlButton} font-taebaek`}
                onClick={steamButtonClickHandler}
              >
                스팀으로 이동
              </button>
              <button
                className={`${styles.urlButton} ${styles.addToCartBtn} font-taebaek`}
                onClick={(event) => handleAddToCart(event)}
              >
                포켓에 담기
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Banner;
