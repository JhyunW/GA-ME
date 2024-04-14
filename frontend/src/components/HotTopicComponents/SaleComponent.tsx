import { useEffect, useState } from 'react';
import style from './SaleComponent.module.css'
import SalesList from './SalesList';
import useHotTopicStore from "../../stores/hotTopicStore";
import useUserStore from "../../stores/userStore";
const SaleComponent: React.FC = () => {
    const { saleData, saleData10, saleData30, saleData50, saleData75, fetchSalesData , sLoading, sError} = useHotTopicStore();
    const [selectedRange, setSelectedRange] = useState('10-30'); // 선택된 할인 범위 상태
    const { user } = useUserStore();
    useEffect(() => {
        if (saleData == null) {
            fetchSalesData(user?.userId ?? 0); // 초기 렌더링 시에만 실행됨            
        }
    }, []);
    const handleRetryClick = () => {
        fetchSalesData(user?.userId ?? 0); // 데이터를 다시 가져오기 위해 호출
    };

    const handleRangeChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSelectedRange(event.target.value);
    };
    if (sLoading) {
        return <div className={`${style.container}`}>
                  <div className={`${style.loader}`}></div>
                  <div>
                    <h1 className={`${style.loadingTitle}`}> 세일 정보를 가져오는 중입니다. </h1>  
                    <h1> 조금만 기다려주세요 ! </h1>  
                  </div>
              </div>;
      }
    
      if (sError) {
        return <div className={`${style.container}`}>
        <div className={`${style.eyes}`}></div>
        <div>
        <h1 className={`${style.loadingTitle}`}> 정보를 가져오는데 실패했습니다. </h1>  
        <button className={`${style.outlineButton}`} onClick={handleRetryClick}> 다시 시도하기 </button>
        </div>
    </div>;
      }
    
      if (!saleData || !saleData.result.length) {
        return <div>No data available</div>;
      }


    return (
        <div className={`${style.saleBox}`}>
            <div className={`${style.select}`}>
                {/* 라디오 버튼 */}
                <input type="radio" id="select" name="shop" value="10-30" checked={selectedRange === '10-30'} onChange={handleRangeChange} />
                <label htmlFor="select"># 10% ~ 30% </label>
                <input type="radio" id="select2" name="shop" value="30-50" checked={selectedRange === '30-50'} onChange={handleRangeChange} />
                <label htmlFor="select2"># 30% ~ 50% </label>
                <input type="radio" id="select3" name="shop" value="50-75" checked={selectedRange === '50-75'} onChange={handleRangeChange} />
                <label htmlFor="select3"># 50% ~ 75% </label>
                <input type="radio" id="select4" name="shop" value="75-" checked={selectedRange === '75-'} onChange={handleRangeChange} />
                <label htmlFor="select4"># 75% ~</label>
            </div>
            <div>
                {/* 선택된 범위에 따라 해당하는 SalesList 컴포넌트를 렌더링 */}
                {selectedRange === '10-30' && (
                    <>
                        <h1 className={`${style.title}`}> 10% ~ 30% 할인 소식</h1>
                        <SalesList cardDtoList={saleData10 || []} />
                    </>
                )}
                {selectedRange === '30-50' && (
                    <>
                        <h1 className={`${style.title1}`}> 30% ~ 50% 할인 소식</h1>
                        <SalesList cardDtoList={saleData30 || []} />
                    </>
                )}
                {selectedRange === '50-75' && (
                    <>
                        <h1 className={`${style.title2}`}> 50% ~ 75% 할인 소식</h1>
                        <SalesList cardDtoList={saleData50 || []} />
                    </>
                )}
                {selectedRange === '75-' && (
                    <>
                        <h1 className={`${style.title3}`}> 75% ~ 할인 소식</h1>
                        <SalesList cardDtoList={saleData75 || []} />
                    </>
                )}
            </div>
        </div>
    );
};

export default SaleComponent;
