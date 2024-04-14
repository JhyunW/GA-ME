import Navbar from "../components/commonUseComponents/Navbar";
import CombinationList from "../components/mixAndMatchComponents/CombinationList";
import Result from "../components/mixAndMatchComponents/Result";
import Title from "../components/mixAndMatchComponents/Title";

function MixAndMatch() {
  return (
    <>
      <div className="flex">
        <Navbar /> {/* 네브바를 화면의 왼쪽에 고정 */}
        {/* <PoketModal /> */}
        <div className="ml-[200px]">
          {" "}
          {/* 네브바 옆으로 메인 컨텐츠를 배치하되, pl-64를 사용하여 네브바의 너비만큼 패딩을 줍니다. */}
          <div className="max-w-full overflow-hidden">
            {" "}
            {/* overflow-hidden을 사용하여 화면 너비를 초과하는 내용이 스크롤되지 않도록 합니다. */}
            <Title />
            <CombinationList />
            <Result />
          </div>
        </div>
      </div>
    </>
  );
}
export default MixAndMatch;
