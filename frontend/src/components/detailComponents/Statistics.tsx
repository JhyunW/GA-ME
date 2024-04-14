// 담당자 : 장현욱

import React, { useEffect, useState } from "react";
import { statisticsDto } from "../../stores/DetailStore";
import ReactApexChart from "react-apexcharts";
import style from "./Statistics.module.css";
import ApexCharts from "apexcharts";
import WordCloud from "./WordCloud";

interface StatisticsProps {
  ratioData: statisticsDto | undefined;
  gameName: string | null;
  gameWordCloudUrl: string | null;
}

const Statistics: React.FC<StatisticsProps> = ({
  ratioData,
  gameName,
  gameWordCloudUrl,
}) => {
  const negativeCounts = ratioData?.negativeCounts ?? [];
  const positiveCounts = ratioData?.positiveCounts ?? [];
  const [resultData, setResultData] = useState<{ x: string; y: number }[]>([]);
  useEffect(() => {
    // 먼저 비율을 계산합니다.
    const resultRatio = positiveCounts.map((positive, index) => {
      const total = positive + negativeCounts[index];
      return total > 0 ? +((positive / total) * 100).toFixed(0) : 0;
    });

    // 비율을 이용해 data 배열을 생성합니다.
    const formattedData = resultRatio?.map((ratio, index) => ({
      x: `${ratioData?.timeValues[index]}`,
      y: ratio,
    }));
    setResultData(formattedData);
  }, []); // 컴포넌트 마운트 시 한 번만 실행

  const series = [
    {
      name: "선호도 비율",
      data: resultData,
    },
  ];
  // 가장 낮은 값 찾기
  const minY = Math.min(...series[0].data.map((item) => item.y));
  // 가장 높은 값 찾기
  const maxY = Math.max(...series[0].data.map((item) => item.y));
  // 가장 높은 값을 가진 X
  const maxYPoint =
    series[0].data.length > 0
      ? series[0].data.reduce(
          (max, p) => (p.y > max.y ? p : max),
          series[0].data[0]
        )
      : { x: "0", y: 0 }; // 배열이 비어있는 경우의 기본값

  // 각 막대의 색상 결정
  const barColors = series[0].data.map((item) => {
    if (item.y === maxY) return "#0451b5"; // Y가 가장 큰 값 색
    if (item.y === minY) return "#fc539f"; // Y가 가장 작은 값 색
    return "#6B7280"; // 나머지 값은 파란색
  });
  const options: ApexCharts.ApexOptions = {
    // 그래프 타입, 크기 지정
    chart: {
      type: "bar",
      height: 380,
      toolbar: {
        show: false, // 차트의 도구 모음을 숨깁니다.
      },
    },

    // 그래프의 제목
    title: {
      text: `이 게임은 ${(parseFloat(maxYPoint.x) / 60).toFixed(
        1
      )}시간 이상 즐겨보시는것을 추천합니다.`,
      offsetX: 30,
      offsetY: 30,
      style: {
        color: "#FFFFFF", // 제목 글자색을 하얀색으로 설정
        fontFamily: "SejonghospitalBold",
        fontSize: "23px",
      },
    },

    // 그래프의 부제목
    subtitle: {
      text: `${gameName}의 시간대별 선호도 비율`, // 부제목에 추가 텍스트를 표시
      offsetX: 30,
      offsetY: 5,
      style: {
        color: "#FFFFFF", // 부제목 글자색을 하얀색으로 설정
        fontFamily: "Pretendard-Regular",
        fontSize: "15px",
      },
    },

    // 그래프 아래(x좌표에) 나올 값
    xaxis: {
      type: "category",
      labels: {
        formatter: function (val: string) {
          const hours = (parseInt(val) / 60).toFixed(1); // 소수점 둘째 자리까지
          return hours + "시간";
        },
        style: {
          colors: "#FFFFFF", // x축 레이블 글자 색을 하얀색으로 설정
          fontSize: "12px", // 선택적: 글자 크기도 조정할 수 있습니다
          // 추가적인 스타일 속성을 여기에 설정할 수 있습니다.
        },
      },
    },

    // 그래프 왼쪽(y좌표에) 나올 값
    yaxis: {
      labels: {
        style: {
          colors: ["#FFFFFF"], // y축 레이블 글자 색을 하얀색으로 설정
        },
      },
    },

    // 각각의 막대에 색을 다르게 하기위한 함수
    plotOptions: {
      bar: {
        horizontal: false, // 가로 세로 바꾸는 함수
        distributed: true, // 각 막대별로 다른 색상 적용
      },
    },
    colors: barColors, // 위의 barColors에 맞게 색상 입히기

    // 호버시 툴팁 옵션
    tooltip: {
      theme: "dark", // 'dark' 테마는 배경을 어둡게 하지만, 여기서는 Gray로 커스텀 필요
      style: {
        // backgroundColor: 'gray', // 툴팁 배경색을 Gray로 설정
        // color: '#FFFFFF', // 툴팁 글자색 설정
        fontSize: "12px",
      },
      y: {
        formatter: function (val: number) {
          // 타입스크립트 정의
          if (val === maxY) {
            return `${val}% - 대유쾌 마운틴`; // 가장 높은 값을 가진 데이터 포인트에 "Best" 추가
          } else if (val === minY) {
            return `${val}% - 노잼사 구간`; // 가장 높은 값을 가진 데이터 포인트에 "Best" 추가
          }
          return `${val}%`;
        },
      },
    },

    // 막대 자체의 속성
    dataLabels: {
      enabled: true,
      //   textAnchor: 'start', 텍스트 위치
      style: {
        colors: ["#FFF"], // 그래프 안의 숫자 색
        fontSize: "12px",
      },
      formatter: function (val: number) {
        // 타입스크립트 형식 구성
        return `${val}%`;
      },
      offsetX: 0, // 왼쪽 마진
      dropShadow: {
        enabled: false,
      },
    },

    // 막대 그래프 테두리
    stroke: {
      width: 1,
      // colors: ['#ebfbff']
    },

    legend: {
      show: false, // 이 부분을 추가하여 레전드를 숨깁니다.
    },
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        width: "100%",
        height: "auto"
      }}
    >
      <div className={`${style.neonBorder} ${style.statisticsContainer}`}>
        <ReactApexChart
          options={options}
          series={series}
          type="bar"
          height={400}
        />
      </div>
      <div
        className={`${style.neonBorder} ${style.statisticsContainer}`}
        style={{ marginTop: "50px" }}
      >
        <WordCloud gameWordCloudUrl={gameWordCloudUrl}></WordCloud>
      </div>
    </div>
  );
};

export default Statistics;
