import React, { useEffect } from "react";
import usePoketStore from "../../stores/poketStore";
import GameCard from "../commonUseComponents/GameCard";
import style from "./MixandMatch.module.css";
import useUserStore from "../../stores/userStore";
import useMixAndMatchStore from "../../stores/mixAndMatchStore";
import { useNavigate } from "react-router-dom"; // useNavigate 훅 추가
import { motion } from "framer-motion";
import { FaPlusSquare, FaArrowRight } from "react-icons/fa";
import { myPageStore } from "../../stores/myPageStore";

const SearchGameList: React.FC = () => {
  const cartItems = usePoketStore((state) => state.cartItems);
  const myPageStores = myPageStore();

  // axios 요청을 위한 requestData 생성
  const userId = useUserStore().user?.userId;
  if (userId) {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    useEffect(() => {
      myPageStores.fetchData(userId);
    }, [myPageStores.fetchData]);
  }
  console.log(myPageStores.data);

  const gameIdAndTagDtoList = [];
  for (const item of cartItems) {
    gameIdAndTagDtoList.push({
      gameId: item.gameId,
      tagList: item.tagsAll,
    });
  }

  // console.log(cartItems)
  const requestData = {
    userId,
    gameIdAndTagDtoList,
  };

  const { fetchData } = useMixAndMatchStore();

  const HandleOnClick = () => {
    fetchData(requestData);
  };

  const navigate = useNavigate(); // useNavigate 인스턴스화

  const handleClickGame = (gameId: number) => {
    navigate(`/detail/${gameId}`);
  };

  const handleGoToMain = () => {
    navigate("/");
  };

  // cartItems가 비어있을 경우 메세지를 표시
  if (cartItems.length === 0) {
    return (
      <div
        className={style.box}
        style={{
          height: "200px",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          flexDirection: "column",
        }}
      >
        <div className={style.getPocketBtn} onClick={handleGoToMain}>
          <FaPlusSquare size={50} className={style.neonEffect} />
          <p className={`mt-[10px] mb-[20px] text-lg ${style.neonEffect}`}>
            게임 담으러 가기😉
          </p>
        </div>
      </div>
    );
  } else if (cartItems.length < 2) {
    return (
      <div className={style.box} style={{ height: "287px" }}>
        <div className={style.gameList}>
          <motion.ul
            className="grid gap-1 grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5"
            variants={{
              hidden: {},
              visible: { transition: { staggerChildren: 0.1 } },
            }}
            initial="hidden"
            animate="visible"
          >
            {cartItems.map((item, index: number) => (
              <motion.li
                key={index}
                className="list-none"
                variants={{
                  hidden: { x: -60, opacity: 0 },
                  visible: { x: 0, opacity: 1, transition: { duration: 0.1 } },
                }}
              >
                <GameCard
                  key={index}
                  gameId={item.gameId}
                  imageUrl={item.imageUrl}
                  title={item.title}
                  price={`${item.price}`}
                  tags={
                    item.tagsAll
                      ?.filter((tag) => tag.codeId === "GEN")
                      .map((tag) => tag.tagName) ?? []
                  }
                  tagsAll={item.tagsAll}
                  likes={item.likes}
                  onGameClick={handleClickGame}
                  isPrefer={item.isPrefer}
                  developer={item.developer}
                  beforPrice={`${item.price}`}
                />
              </motion.li>
            ))}

            <p
              className={`${style.infoText} ${style.neonEffect}`} onClick={handleGoToMain} 
            >
              게임을 2개 이상 담아 주세요😉
              <br/>
              게임 담으러 가기
              <FaArrowRight size={18} style={{display:'inline-block', marginLeft:'5px'}}/>
            </p>
          </motion.ul>
        </div>
      </div>
    );
  }

  return (
    <div className={style.box} style={{ height: "287px" }}>
      <div className={style.gameList}>
        <motion.ul
          className="grid gap-1 grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5"
          variants={{
            hidden: {},
            visible: { transition: { staggerChildren: 0.1 } },
          }}
          initial="hidden"
          animate="visible"
        >
          {cartItems.map((item, index: number) => (
            <motion.li
              key={index}
              className="list-none"
              variants={{
                hidden: { x: -60, opacity: 0 },
                visible: { x: 0, opacity: 1, transition: { duration: 0.1 } },
              }}
            >
              <GameCard
                key={index}
                gameId={item.gameId}
                imageUrl={item.imageUrl}
                title={item.title}
                price={`${item.price}`}
                tags={
                  item.tagsAll
                    ?.filter((tag) => tag.codeId === "GEN")
                    .map((tag) => tag.tagName) ?? []
                }
                tagsAll={item.tagsAll}
                likes={item.likes}
                onGameClick={handleClickGame}
                isPrefer={item.isPrefer}
                developer={item.developer}
                beforPrice={`${item.price}`}
              />
            </motion.li>
          ))}
        </motion.ul>
      </div>
      <button className={style.mixBtn} onClick={HandleOnClick}>
        {" "}
        Mix!{" "}
      </button>
    </div>
  );
};

export default SearchGameList;
