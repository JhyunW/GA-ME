import { useState, useEffect } from "react";
import DetailInfo from "../components/detailComponents/DetailInfo";
import Statistics from "../components/detailComponents/Statistics";
import BackButton from "../components/detailComponents/BackButton";
import Poket from "../components/commonUseComponents/Poket";
import DetailBanner from "../components/detailComponents/DetailBanner";
import styles from "../components/detailComponents/Detail.module.css";
import { useDetailStore } from "../stores/DetailStore";
import { useParams } from "react-router-dom";
import useUserStore from "../stores/userStore";
import { api, log } from "../url/api";

type ActiveComponentType = "info" | "statistics";

function Detail(): JSX.Element {
  const { gameId } = useParams<{ gameId: string }>();
  const parsedGameId = gameId ? parseInt(gameId, 10) : undefined;
  const { user } = useUserStore();

  const [activeComponent, setActiveComponent] =
    useState<ActiveComponentType>("info");
  const { data, statisticsResult, statisticsData, fetchData } =
    useDetailStore();

  const handleInfoClick = () => {
    setActiveComponent("info");
  };

  const handleStatisticsClick = () => {
    setActiveComponent("statistics");
  };

  useEffect(() => {
    // userId와 gameId를 설정하고 fetchData 함수를 호출하여 데이터를 가져옴
    fetchData(parsedGameId, user?.userId?? 0);
    statisticsData(parsedGameId);
    window.scrollTo({
      top: 0,
      behavior: "smooth", // 스크롤 부드럽게 이동
    });

    if (user) {
      //가중치 증가(detail 이동)
      api.put("/tracking?userId=" + user.userId+"&gameId="+gameId+"&action=detail");

      // 사용자 패턴 로그
      log(user?.userId, "somewhere", "move", [
        { move_page: "detail" },
        { game_id: gameId },
      ]);
    }
  }, [parsedGameId]); // 컴포넌트가 마운트될 때 한 번만 fetchData 함수 호출

  return (
    <>
      <Poket />
      <BackButton />
      <DetailBanner
        bannerImage={data?.result?.gameHeaderImg ?? ""}
        gameId={data?.result?.gameId || 0}
        gameName={data?.result?.gameName ?? ""}
        gameShortDescription={data?.result?.gameShortDescription}
        gameIsLike={data?.result?.gameIsLike}
        price={`₩ ${data?.result?.gamePriceFinal}` ?? ""}
        developer={data?.result?.gameDeveloper ?? ""}
        tagsAll={data?.result?.gameTagList} 
        likes={0} 
        isPrefer={data?.result?.gameIsLike ?? false}      />
      <div className={styles.detailContent}>
        <div className={styles.menuTab}>
          <button
            className={`${
              activeComponent === "info"
                ? styles.activeButton
                : styles.inActiveButton
            } font-sejong`}
            onClick={handleInfoClick}
          >
            정보
          </button>
          <span className={styles.dot}>·</span>
          <button
            className={`${
              activeComponent === "statistics"
                ? styles.activeButton
                : styles.inActiveButton
            } font-sejong`}
            onClick={handleStatisticsClick}
          >
            통계
          </button>
        </div>
        <div style={{marginTop:'15px'}}>
          {activeComponent === "info" && <DetailInfo data={data?.result} />}
          {activeComponent === "statistics" && (
            <Statistics
              ratioData={statisticsResult?.result.statisticsDto}
              gameName={data?.result?.gameName ?? ""}
              gameWordCloudUrl={statisticsResult?.result.gameWordCloudUrl ?? null}
            />
          )}
        </div>
      </div>
    </>
  );
}

export default Detail;
