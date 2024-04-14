// 장현욱
import { useNavigate } from 'react-router-dom';

const BackButton: React.FC = () => {
    const navigate = useNavigate();

  return (
    <button className="fixed m-12 cursor-pointer duration-200 hover:scale-125 active:scale-100"
    style={{ zIndex: 999 }} // 여기에 zIndex를 직접 지정
    title="Go Back"
    onClick={() => navigate(-1)}>
  <svg xmlns="http://www.w3.org/2000/svg" width="50px" height="50px" viewBox="0 0 24 24" className="stroke-white">
    <path stroke-linejoin="round" stroke-linecap="round" stroke-width="1.5" d="M11 6L5 12M5 12L11 18M5 12H19"></path>
  </svg>
</button>
  );
};

export default BackButton;