// 담당자 : 장현욱


import { motion } from "framer-motion";
import { MdCheckCircle } from "react-icons/md"; // 체크 아이콘 import

// 타입스크립트 타입 프롭받을 타입 정의
export interface SimpleGameCardProps {
  imageUrl: string;
  title: string | JSX.Element;
  gameId: number;
  isSelected?: boolean;
}

const SimpleGameCard: React.FC<SimpleGameCardProps> = ({
  imageUrl,
  title,
  isSelected = false,
}) => {
  return (
    <motion.div
      className="w-48 rounded overflow-hidden shadow-lg transform cursor-pointer relative"
      whileHover={{ scale: 1.05 }}
      transition={{ type: "spring", stiffness: 300 }}
    >
      {isSelected && (
        <div className="absolute top-0 left-0 right-0 bottom-0 bg-black bg-opacity-50 flex justify-center items-center">
          <MdCheckCircle className="text-white text-5xl" /> {/* 체크 아이콘 */}
        </div>
      )}
      <div
        className={`w-48 h-26 rounded overflow-hidden bg-gray-600 text-white text-center`}
      >
        <img
          src={imageUrl}
          className="object-cover w-50 h-50"
          style={{ width: "192px", height: "110px", objectFit: "cover" }}
        />
        <div className="p-1">
          <h3 className="text-sm truncate" title={typeof title === 'string' ? title : undefined}>{title}</h3>
        </div>
      </div>
    </motion.div>
  );
};

export default SimpleGameCard;
