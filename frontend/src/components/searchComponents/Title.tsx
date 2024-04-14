import style from "./Title.module.css";
import useUserStore from "../../stores/userStore";

const Title: React.FC = () => {
  const { user } = useUserStore();

  return (
    <div className={`${style.titleDiv}`}>
      <h1 className={`${style.title}`}>Search</h1>
      {/* <img className={`${style.icon}`} src="./SearchIcon.png"></img> */}
      {/* 사용자 이름을 환영 메시지에 포함 */}
      {user && (
        <p className={`${style.detail}`}>
          {user.userName}님! 원하는 게임을 찾아보세요!
        </p>
      )}
      {/* 사용자 정보가 없을 경우 기본 메시지 표시 */}
      {!user && <p className={`${style.detail}`}>원하는 게임을 찾아보세요!</p>}
    </div>
  );
};

export default Title;
