import { motion } from 'framer-motion'
import style from './NewsCard.module.css'
import { useState } from 'react';

// 타입스크립트 타입 프롭받을 타입 정의
interface NewsCardProps {
  hotTopicLink: string;
  hotTopicImg: string;
  hotTopicTitle: string;
  hotTopicShortDesc: string;
  hotTopicDate: Date;
}

// 타입스크립트식 선언
const NewsCardDto: React.FC<NewsCardProps> = ({ hotTopicLink, hotTopicImg, hotTopicTitle, hotTopicShortDesc }) => {

  const [isHovered, setIsHovered] = useState(false);
  isHovered;
  const hoverEffects = {
    scale: [1, 1.02], // 호버시 크기 설정
    transition: { duration: 0.3 },
  };
  const handleCardClick = () => {
    window.open(hotTopicLink, '_blank'); // 새 창으로 링크 열기
  };
  // hotTopicShortDesc를 일정 길이로 자르고 생략 부호(...)를 추가하는 함수
  const truncateString = (str: string, maxLength: number) => {
    if (str.length > maxLength) {
      return str.slice(0, maxLength) + ' · · · ';
    } else {
      return str;
    }
  };
  return (
    <motion.div
      className={`${style.card} m-2 rounded overflow-hidden text-white text-center relative cursor-pointer`}
      whileHover={hoverEffects}
      onClick={handleCardClick}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      <div className={`${style.card}  rounded overflow-hidden text-white text-center`}>
        <img src={hotTopicImg} alt={hotTopicTitle} className={`${style.cardImg} w-full`} />
        <div className="p-2">
          <h3 className={`${style.baseFont} text-base`}>{hotTopicTitle}</h3>
          <p className="text-xs">{truncateString(hotTopicShortDesc, 130)}</p>
        </div>
      </div>
    </motion.div>
  );
};

export default NewsCardDto;