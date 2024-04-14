import { useEffect } from "react";
import Navbar from "../components/commonUseComponents/Navbar";
import Poket from "../components/commonUseComponents/Poket";
import Banner from "../components/mainComponents/Banner";
import Game from "../components/mainComponents/Game"
import Select from "../components/mainComponents/Select"
import useUserStore from "../stores/userStore";
import useHotTopicStore from "../stores/hotTopicStore";
function Main() {  
  const { user } = useUserStore();
  const { newsData, nLoading, newsFetched,fetchNewsData } = useHotTopicStore();
  useEffect(() => {
    if (user && !newsFetched && !nLoading&& newsData==null) {
      fetchNewsData(user.userId);
    }
  }, [user]); // 두 번째 매개변수로 빈 배열 전달
  return (
    
    <>
    <div className="flex">
    <Navbar /> {/* 화면의 왼쪽에 고정 */}
    <Poket />
    <div className="pl-72 pr-36 w-full"> 
    <div className="max-w-full overflow-hidden">
        <Banner />
        <Select />
        <Game />
    </div>
    </div>
    </div>
    </>
  );
}
export default Main;
