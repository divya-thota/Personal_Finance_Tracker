import { ResponsiveBar } from "@nivo/bar";

const InvestmentProfitsChart = () => {
const data = [
  { stock: "AAPL", investment: 5000, profit: 1500 },
  { stock: "TSLA", investment: 4000, profit: 800 },
  { stock: "GOOGL", investment: 3000, profit: 600 },
  { stock: "AMZN", investment: 6000, profit: 2000 },
];

  return (
    <div style={{ height: "300px" }}>
      <ResponsiveBar
        data={data}
        keys={["investment", "profit"]}
        indexBy="stock"
        margin={{ top: 50, right: 0, bottom: 50, left: 50 }}
        padding={0.3}
        enableTotals={true}
        colors={["#fbb4ae", "#ccebc5"]}
        axisBottom={{ legend: "Stock", legendOffset: 36, legendPosition: "middle" }}
        axisLeft={{ legend: "Amount ($)", legendOffset: -45, legendPosition: "middle" }}
        legends={[
                  {
                    dataFrom: "keys",
                    anchor: "top",
                    direction: "row",
                    justify: false,
                    translateX: 0,
                    translateY: -40,
                    itemsSpacing: 2,
                    itemWidth: 100,
                    itemHeight: 20,
                    itemDirection: "left-to-right",
                    itemOpacity: 0.85,
                    symbolSize: 20,
                    effects: [
                      {
                        on: "hover",
                        style: { itemOpacity: 1 },
                      },
                    ],
                  },
                ]}
      />
    </div>
  );
};

export default InvestmentProfitsChart;
