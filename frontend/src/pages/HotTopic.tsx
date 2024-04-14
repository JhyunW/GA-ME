import React, { useState, useEffect } from "react";
import Navbar from "../components/commonUseComponents/Navbar";
import Title from "../components/HotTopicComponents/Title";
import SaleButton from "../components/HotTopicComponents/SaleButton";
import NewsButton from "../components/HotTopicComponents/NewsButton";
import NewsList from "../components/HotTopicComponents/NewsList";
import SaleComponent from "../components/HotTopicComponents/SaleComponent";
import useHotTopicStore from "../stores/hotTopicStore";
import Poket from "../components/commonUseComponents/Poket";
import useUserStore from "../stores/userStore";
const HotTopic: React.FC = () => {
  const [showSale, setShowSale] = useState(true);
  const { user } = useUserStore();
  const { newsData, nLoading, newsFetched,fetchNewsData } = useHotTopicStore();

  useEffect(() => {
    if (user && !newsFetched && !nLoading&& newsData==null) {
      console.log("news 요청")
      fetchNewsData(user.userId);
    }
  }, []); // 두 번째 매개변수로 빈 배열 전달

  const handleNewsButtonClick = () => {
    setShowSale(false);
  };

  const handleSaleButtonClick = () => {
    setShowSale(true);
  };
  return (
    <>
      <Navbar />
      {showSale && <Poket />}
      <div className="pl-80 pr-36 w-full">
        <Title />
        <div style={{ marginTop: "20px", display: "flex", gap: "20px" }}>
          <SaleButton onClick={handleSaleButtonClick} />
          <NewsButton onClick={handleNewsButtonClick} />
        </div>
        <div style={{ marginTop: "30px", display: "flex" }}>
          {!showSale && <NewsList />}
          {showSale && <SaleComponent />}
        </div>
      </div>
    </>
  );
};

export default HotTopic;
