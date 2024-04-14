import { TagWeight, myPageStore } from "../../stores/myPageStore";
import { AxiosError } from "axios";
import LikeComponent from "./Like";
import StatisticsComponent from "./Statistics";
import styles from "./MyPage.module.css";
import { useEffect } from "react";
import useUserStore from "../../stores/userStore";

const MyProfile: React.FC = () => {
  const { user } = useUserStore();
  const { data, topTenTag, error, loading, fetchData } = myPageStore();
  

  if (user) {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    useEffect(() => {
      fetchData(user.userId);
    }, [fetchData, user]);
    console.log(topTenTag);

    if (loading) {
      return (
        <button type="button" className="bg-indigo-500 ..." disabled>
          <svg className="animate-spin h-5 w-5 mr-3 ..." viewBox="0 0 24 24">
            Processing...
          </svg>
        </button>
      );
    }
    if (error) {
      const axiosError = error as AxiosError;
      return <div>Error: {axiosError.message}</div>;
    }
    return (
      <>
        <div style={{paddingTop: '35px'}}>
          <div className="relative " style={{ bottom: "80px", left: "30px",}}>
            <img
              className=" rounded-full"
              style={{ width: "8rem", height: "8rem" }}
              src={data.result.userProfileImg}
              alt=""
            />
          </div>

          <div className="flex items-center h-screen">
            <div
              className="rounded-xl items-center"
              style={{
                paddingTop: '10px',
                marginBottom: "10%",
                maxHeight: "850px",
                // maxWidth: "900px",
                width: "1000px",
              }}
            >
              <div
                className="rounded-2xl"
                style={{
                  padding: "40px",
                  border: "3px solid white",
                  backgroundColor: "black",
                }}
              >
                <div className={`${styles.userName} font-sejong mt-3`}>
                  {data.result.userName}
                </div>
                {topTenTag.map((tag: TagWeight, index: number) => (
                  <span
                    key={index}
                    className="bg-tag-gray inline-block px-2 py-1 rounded-[3px] ml-3 font-Pretendard"
                    style={{ backgroundColor: "#036280", margin: "4px" }}
                  >
                    #{tag.tagName}{" "}
                  </span>
                ))}         
                <hr className="mt-8 mb-8"/>            
                <LikeComponent />
                <StatisticsComponent />
              </div>
            </div>
          </div>
        </div>
      </>
    );
  }
};

export default MyProfile;
