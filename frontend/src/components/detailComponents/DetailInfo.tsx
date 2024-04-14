import React from "react";
import InfoDetailDesc from "./InfoDetailDesc";
import InfoYoutube from "./InfoYoutube";
import InfoGame from "./InfoGame";
import InfoDescription from "./InfoDescription";
import InfoScreenshot from "./InfoScreenshot";
import styles from "./DetailInfo.module.css";

import { GameData } from "../../stores/DetailStore";

interface DetailInfoProps {
  userId?: number;
  data: GameData | undefined; // GameData 타입의 배열 또는 null
}

// DetailInfo 컴포넌트를 정의합니다.
const DetailInfo: React.FC<DetailInfoProps> = ({ userId, data }) => {
  const isGame = !!data?.relatedGameList;
  return (
    <>
      <div className={styles.infoContainer}>
        {data && (
          <>
            <InfoScreenshot screenshots={data?.screenshotList} />
            {/* InfoDetailDesc 컴포넌트에 data를 통째로 내려줍니다 */}
            <InfoDetailDesc data={data} />

            {/* InfoYoutube 컴포넌트는 따로 데이터를 내려주지 않습니다 */}
            <InfoYoutube
              userId={userId}
              gameId={data.gameId}
              gameName={data.gameName}
            />

            {/* data.relatedGameList가 유효한 경우에만 InfoGame 컴포넌트를 렌더링합니다 */}
            <InfoGame relatedGameList={data?.relatedGameList} isGame={isGame} />

            {/* InfoDescription 컴포넌트에는 gameDetailedDescription을 내려줍니다 */}
            <InfoDescription
              gameDetailedDescription={data.gameDetailedDescription}
            />
          </>
        )}
      </div>
    </>
  );
};

export default DetailInfo;
