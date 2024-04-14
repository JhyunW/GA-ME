import Poket from "../components/commonUseComponents/Poket";
import Profile from "../components/MyPageComponents/Profile";
import Navbar from "../components/commonUseComponents/Navbar";

function MyPage() {
  return (
    <>
      <Navbar /> {/* 네브바를 화면의 왼쪽에 고정 */}
      <div style={{ position: "relative"}}>
        <Poket />
        <div style={{ position: "absolute", left: "350px", top: "100px" }}>
          <Profile />
        </div>
      </div>
    </>
  );
}
export default MyPage;
