import { ResponsivePie } from "@nivo/pie";

const InvestmentPieChart = () => {
const stockData = [
  { id: "AAPL", value: 5000 },
  { id: "TSLA", value: 4000 },
  { id: "GOOGL", value: 3000 },
  { id: "AMZN", value: 6000 },
];

  return (
    <div style={{ height: "300px" }}>
      <ResponsivePie
              data={stockData}
              margin={{ top: 40, right: 80, bottom: 60, left: 80 }}
              innerRadius={0.5}
              padAngle={2}
              cornerRadius={3}
              colors={{ scheme: "pastel1" }}
              borderWidth={1}
              borderColor={{ from: "color", modifiers: [["darker", 0.2]] }}
              arcLabelsSkipAngle={10}
              arcLinkLabelsSkipAngle={10}
              arcLinkLabelsTextColor="#333333"
              arcLinkLabelsOffset={5}
              legends={[
                {
                  anchor: "bottom",
                  direction: "row",
                  translateY: 50,
                  itemWidth: 100,
                  itemHeight: 14,
                  itemTextColor: "#333",
                  symbolSize: 14,
                },
              ]}
            />
    </div>
  );
};

export default InvestmentPieChart;
