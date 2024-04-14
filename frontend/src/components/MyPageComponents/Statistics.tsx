import { useEffect } from "react";
import { myPageStore } from "../../stores/myPageStore";

interface opts {
  seriesIndex: number;
}

const statisticsComponent: React.FC = () => {
  const { data, topTenTag } = myPageStore();

  const userTagWeights: number[] = topTenTag
    .slice(0, 9)
    .map((item) => item.userTagWeight);
  const userGameName: string[] = topTenTag
    .slice(0, 9)
    .map((item) => item.tagName);

  const otherWeight: number[] = data.result.tagWeightList
    .map((item) => item.userTagWeight)
    .filter((weight) => weight >= 0);

  // 유저가 선호하는 태그를 제외한 나머지의 모든 합
  if (otherWeight.length >= 9) {
    userTagWeights.push(0);
    userGameName.push("Others");
    for (let i = 9; i < otherWeight.length; i++)
      userTagWeights[userTagWeights.length - 1] += otherWeight[i];
  }

  useEffect(() => {
    const options = {
      series: userTagWeights,
      chart: {
        width: 500,
        type: "donut",

        foreColor: "f9fafb",
      },
      plotOptions: {
        pie: {
          startAngle: -90,
          endAngle: 270,
        },
      },
      labels: [...userGameName],
      dataLabels: {
        enabled: true,
        formatter: function (val: number) {
          return `${Math.floor(val)}%`;
        },
      },
      fill: {
        type: "gradient",
      },
      legend: {
        fontFamily: "SejonghospitalBold, sans-serif",
        fontSize: "12px",
        position: "bottom",
        formatter: function (_val: number, opts: opts) {
          return `${userGameName[opts.seriesIndex]}`;
        },
      },
      responsive: [
        {
          breakpoint: 480,
          options: { chart: { width: 200 } },
        },
      ],
      noData: { text: "Loading..." },
    };

    const chart = new ApexCharts(document.getElementById("chart"), options);
    chart.render();

    // Cleanup function
    return () => {
      chart.destroy(); // Destroy the chart instance to prevent memory leaks
    };
  }, [userTagWeights, userGameName]); // Depend on any variables that might change and affect the chart
  return (
    <>
      <h1 className="text-2xl font-sejong">나의 취향 분석</h1>
      <div id="chart" className="flex flex-col items-center"></div>
    </>
  );
};

export default statisticsComponent;
