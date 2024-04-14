import React from "react";
import useMixAndMatchStore from "../../stores/mixAndMatchStore";
import GameCard from "../commonUseComponents/GameCard";
import style from "./MixandMatch.module.css";
import { useNavigate } from "react-router-dom"; // useNavigate 훅 추가
import { motion } from "framer-motion";

const Result: React.FC = () => {
  const { results, loading } = useMixAndMatchStore(); // 로딩 상태 추가
  const navigate = useNavigate(); // useNavigate 인스턴스화

  const gameCardDtoList = results?.gameCardDtoList;

  const handleClickGame = (gameId: number) => {
    navigate(`/detail/${gameId}`);
  };

  return (
    <div>
      {/* 데이터가 로딩되지 않은 경우에만 문구를 표시합니다. */}
      {!loading && !gameCardDtoList && (
        <div>
          <p className={style.beforeMixText}>
            어떤 게임을 조합해 볼까요?
          </p>
        </div>
      )}

      {/* 로딩 중인 경우 스피너 표시 */}
      {loading && (
        <div className={`${style.container}`}>
          <img
            src="/MixAndMatch.gif"
            alt="Mix and Match"
            className={`${style.loadingImg}`}
          />
          <div>
            <h1 className={`${style.loadingTitle}`}>뚝딱뚝딱 조합 중... </h1>
            <p className={style.loadingSubTitle}>
              {" "}
              &nbsp; 조금만 기다려주세요{" "}
            </p>
          </div>
        </div>
      )}

      {/* 로딩이 완료되면 게임 카드 표시 */}
      {!loading && gameCardDtoList && (
        <>
          <p className="ml-[185px] mt-[30px] text-[30px] font-sejong">Match</p>
          <div className={style.box} style={{ marginTop: 0 }}>
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
                {gameCardDtoList.map((item, index: number) => (
                  <motion.li
                    key={index}
                    className="list-none"
                    variants={{
                      hidden: { x: -60, opacity: 0 },
                      visible: {
                        x: 0,
                        opacity: 1,
                        transition: { duration: 0.1 },
                      },
                    }}
                  >
                    <GameCard
                      key={index}
                      gameId={item.gameId}
                      imageUrl={item.gameHeaderImg}
                      title={item.gameName}
                      developer={item.gameDeveloper}
                      price={`₩ ${item.gamePriceFinal/100}`}
                      beforPrice={`₩ ${item.gamePriceInitial/100}`}
                      tags={item.tagList
                        .filter((tag) => tag.codeId === "GEN")
                        .map((tag) => tag.tagName)}
                      tagsAll={item.tagList}
                      likes={item.gameLike}
                      isPrefer={item.isPrefer}
                      onGameClick={() => handleClickGame(item.gameId)}
                    />
                  </motion.li>
                ))}
              </motion.ul>
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default Result;
