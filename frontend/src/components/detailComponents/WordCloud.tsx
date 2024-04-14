import React, { useState } from "react";
import styles from "./WordCloud.module.css";

interface WordCloudProps {
  gameWordCloudUrl: string | null; // 또는 undefined, 사용하는 컴포넌트의 상황에 맞춰 조정
}

const InfoDescription: React.FC<WordCloudProps> = ({ gameWordCloudUrl }) => {
  // 이미지 로딩 실패를 추적하는 상태
  const [imageError, setImageError] = useState(false);

  return (
    <div className={styles.container}>
      <h1 className={styles.reviewTitle}>리뷰 분석</h1>
      <p className={styles.reviewDetail}>리뷰에 많이 사용된 단어</p>
      {gameWordCloudUrl && !imageError ? (
        <img
          className={styles.wordCloudImg}
          src={gameWordCloudUrl}
          alt="Game Word Cloud"
          onError={() => setImageError(true)} // 이미지 로딩 실패 시 imageError 상태를 true로 설정
        />
      ) : (
        <div className={styles.wordCloudContainer}>
          <p className={styles.noImg}>분석을 할만한 충분한 댓글 데이터가 없습니다 💦</p>
        </div>
      )}
    </div>
  );
};

export default InfoDescription;
