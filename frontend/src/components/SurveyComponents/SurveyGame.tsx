import { useEffect, useState } from "react";
import { surveyStore } from "../../stores/surveyStore";
import styles from "./SurveyGame.module.css";
import { motion } from "framer-motion";
import SimpleGameCard from "../commonUseComponents/SimpleGameCard";
import { AxiosError } from "axios";
import { ConfigProvider, Steps } from "antd";
import useUserStore from "../../stores/userStore";
import { useNavigate } from "react-router-dom";
import { myPageStore } from "../../stores/myPageStore";
import Swal from "sweetalert2";

const { Step } = Steps;

export interface ChoiceGame {
  gameId: number;
  gameChoiceName: string;
  gameHeaderImg: string;
}

const SurveyGame: React.FC = () => {
  const navigate = useNavigate();
  // checkGameList 내부에 survey 페이지에서 선택한 게임 정보가 들어있다.
  const {
    data,
    loading,
    error,
    checkGameList,
    backGroundImg,
    setBackgroundImg,
    fetchData,
    addCheckChoiceGame,
    removeCheckChoiceGame,
  } = surveyStore();
  const { user } = useUserStore();
  const [current, setCurrent] = useState(0);
  const { addLikeWeight } = myPageStore();
  useEffect(() => {
    fetchData(); // 마운트시 데이터 가져오기
  }, [fetchData, user]); // 데이터 변경시 재랜더링
  // 이 시점에 data에 정보가 들어와있음
  const oneList: ChoiceGame[] = [];
  const twoList: ChoiceGame[] = [];
  const threeList: ChoiceGame[] = [];

  const groups: ChoiceGame[][] = [];

  groups.push(oneList);
  groups.push(twoList);
  groups.push(threeList);
  if (data)
    for (let i = 0; i < 3; i++)
      for (let j = i * 12; j < i * 12 + 12; j++) groups[i].push(data.result[j]);

  const changeBackgroundImg = (image: string) => {
    setBackgroundImg(image);
  };

  const stepValidation = () => {
    if (checkGameList[current].length !== 0) return true;
    return false;
  };

  const isInGameList = (gameId: number, current: number) => {
    if (checkGameList[current].includes(gameId)) return true;
    return false;
  };

  const changeGameList = (gameId: number, current: number, image: string) => {
    changeBackgroundImg(image);
    // 존재한다면 배열에서 게임을 없앤다.
    if (isInGameList(gameId, current)) removeCheckChoiceGame(gameId, current);
    // 존재하지 않는다면 배열에 추가한다.
    else addCheckChoiceGame(gameId, current);
  };

  const clickSubmit = () => {
    if (!user) {
      Swal.fire({
        icon: "error",
        title: "오류",
        text: "유저 정보가 존재하지 않습니다.",
      });
      navigate("/");
      throw new Error("유저 정보가 존재하지 않습니다.");
    }

    if (stepValidation()) {
      addLikeWeight(user.userId, checkGameList);
      Swal.fire({
        icon: "success",
        title: "환영합니다!",
        text: `${user?.userName}님! GA:ME에게 게임을 추천받아 보세요!`,
      }).then(() => {
        navigate("/");
      });
    } else
      Swal.fire({
        icon: "warning",
        title: "알림",
        text: "게임을 최소 하나를 선택해주세요!",
      });
  };

  const nextClick = () => {
    if (stepValidation()) setCurrent(current + 1);
    else
      Swal.fire({
        icon: "warning",
        title: "알림",
        text: "게임을 최소 하나를 선택해주세요!",
      });
  };

  const prevClick = () => {
    setCurrent(current - 1);
  };

  // 마지막 페이지라면 Submit 버튼 활성화
  if (loading) {
    return (
      <button type="button" className="bg-indigo-500 ..." disabled>
        <svg className="animate-spin h-5 w-5 mr-3 ..." viewBox="0 0 24 24">
          Processing...
        </svg>
      </button>
    );
  }

  if (error) {
    const axiosError = error as AxiosError;
    return <div>Error: {axiosError.message}</div>;
  }

  if (!data || !data.result.length) {
    return <div>No data available</div>;
  }

  return (
    <>
      <ConfigProvider
        theme={{
          components: {
            Steps: {
              navArrowColor: "#D9D9D9",
              colorPrimary: "#036280",
            },
          },
        }}
      >
        <div className={styles.container}>
          {/* 배경 이미지와 필터를 적용하는 div */}
          <div
            className={styles.backgroundImage}
            style={{ backgroundImage: `url(${backGroundImg})` }}
          />
          <div className={styles.contentWrapper}></div>
          <div className={styles.contentWrapper}>
            <div className="flex justify-center items-center h-full">
              <div className="relative w-900px h-700px bg-box-gray rounded-lg p-4 pl-8 pr-8">
                {/* 내용 */}
                <div className="bg-white-500">
                  <Steps
                    type="navigation"
                    onChange={stepValidation}
                    current={current}
                    style={{ color: "#D9D9D9" }}
                  >
                    <Step status={current >= 0 ? "process" : "wait"} />
                    <Step status="process" />
                    <Step status="process" />
                  </Steps>
                </div>
                <p className="text-white mt-[20px] mb-[20px]">
                  맞춤 추천을 위해 당신의 게임 취향을 알려주세요!
                </p>
                <div className="bg-box ">
                  <div className="grid grid-cols-4 gap-3">
                    {groups[current].map(
                      (choiceGame: ChoiceGame, index: number) => (
                        <motion.li
                          key={index}
                          className={"list-none "}
                          variants={{
                            hidden: { x: -60, opacity: 0 },
                            visible: {
                              x: 0,
                              opacity: 1,
                              transition: { duration: 0.3 },
                            },
                          }}
                          onClick={() =>
                            changeGameList(
                              choiceGame.gameId,
                              current,
                              choiceGame.gameHeaderImg
                            )
                          }
                        >
                          <SimpleGameCard
                            key={index}
                            gameId={choiceGame.gameId}
                            imageUrl={choiceGame.gameHeaderImg}
                            title={choiceGame.gameChoiceName}
                            isSelected={isInGameList(
                              choiceGame.gameId,
                              current
                            )} // 선택 상태 전달
                          />
                        </motion.li>
                      )
                    )}
                  </div>
                </div>
                {current === 2 && (
                  <>
                    <button
                      className="bg-secondary hover:bg-primary text-white font-bold py-2 px-4 mt-8 rounded-full"
                      onClick={prevClick}
                    >
                      Prev
                    </button>
                    <button
                      className="bg-secondary hover:bg-primary text-white font-bold py-2 px-4 mt-8 ml-4 rounded-full"
                      onClick={clickSubmit}
                    >
                      Submit
                    </button>
                  </>
                )}
                {current === 1 && (
                  <>
                    <button
                      className="bg-secondary hover:bg-primary text-white font-bold py-2 px-4 mt-8 rounded-full"
                      onClick={prevClick}
                    >
                      Prev
                    </button>
                    <button
                      className="bg-secondary hover:bg-primary text-white font-bold py-2 px-4 mt-8 ml-4 rounded-full"
                      onClick={nextClick}
                    >
                      Next
                    </button>
                  </>
                )}
                {current === 0 && (
                  <>
                    <button
                      className="bg-secondary hover:bg-primary text-white font-bold py-2 px-4 mt-8 rounded-full"
                      onClick={nextClick}
                    >
                      Next
                    </button>
                  </>
                )}
              </div>
            </div>
          </div>
        </div>
      </ConfigProvider>
    </>
  );
};

export default SurveyGame;
