import React from "react";
import styles from "./InfoDescription.module.css";

interface InfoDescriptionProps {
  gameDetailedDescription: string;
}

const InfoDescription: React.FC<InfoDescriptionProps> = ({
  gameDetailedDescription,
}) => {
  return (
    <>
      <div className={styles.container}>
        <div className={`${styles.title} font-sejong`}>게임 설명</div>
        <div
          className={styles.desc}
          dangerouslySetInnerHTML={{ __html: gameDetailedDescription }}
        ></div>
      </div>
    </>
  );
};

export default InfoDescription;
