import React, { useState, useEffect } from "react";
import styles from "./InfoScreenshot.module.css";
import { Screenshot } from "../../stores/DetailStore";

interface InfoScreenshotProps {
  screenshots: Screenshot[]; // 이미지 URL 배열
}

const InfoScreenshot: React.FC<InfoScreenshotProps> = ({ screenshots }) => {
  const [selectedImageIndex, setSelectedImageIndex] = useState(0);
  const [timerActive, setTimerActive] = useState(true);

  // 이미지 리스트를 클릭했을 때 왼쪽 이미지 변경
  const handleImageItemClick = (index: number) => {
    setSelectedImageIndex(index);
  };

  useEffect(() => {
    setSelectedImageIndex(0); // 컴포넌트가 마운트될 때 스크린샷 인덱스 초기화
  }, [screenshots?.length]);
  useEffect(() => {
    if (timerActive) {
      const intervalId = setInterval(() => {
        setSelectedImageIndex((prevIdx) => (prevIdx + 1) % screenshots.length);
      }, 4000); // 3초마다 이미지 변경

      return () => clearInterval(intervalId); // 컴포넌트가 unmount되면 clearInterval로 interval 제거
    }
  }, [screenshots?.length, timerActive]);

  // 메인 이미지에 마우스가 올라갔을 때 타이머 중지
  const handleMouseEnter = () => {
    setTimerActive(false);
  };

  // 메인 이미지에서 마우스가 떠났을 때 타이머 다시 시작
  const handleMouseLeave = () => {
    setTimerActive(true);
  };

  if (screenshots.length === 0) {
    return (
      <div className={styles.container}>
        <img
          className={styles.defaultImg}
          src="/DefaultScreenShotImg.webp"
          alt="default-screenshot-img"
        />
      </div>
    );
  }

  return (
    <div
      className={styles.container}
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
    >
      <div className={styles.imageContainer}>
        <img
          src={screenshots[selectedImageIndex]?.path_full}
          alt="Screenshot"
          className={styles.mainImage}
        />
      </div>
      <div className={styles.imageListContainer}>
        <div className={styles.imageList}>
          {screenshots.map((screenshot, index) => (
            <img
              key={index}
              src={screenshot.path_thumbnail}
              alt={`Screenshot ${index}`}
              className={`${styles.imageItem} ${
                index === selectedImageIndex ? styles.active : ""
              }`}
              onClick={() => handleImageItemClick(index)}
            />
          ))}
        </div>
      </div>
    </div>
  );
};

export default InfoScreenshot;
