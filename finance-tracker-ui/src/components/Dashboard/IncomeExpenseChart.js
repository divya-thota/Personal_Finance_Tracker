import { ResponsiveBar } from "@nivo/bar";
import { ResponsiveLine } from "@nivo/line";

const data = [
  { month: "Jan", income: 4000, expenses: 2500, profit: 1500 },
  { month: "Feb", income: 4200, expenses: 2700, profit: 1500 },
  { month: "Mar", income: 3900, expenses: 2600, profit: 1300 },
  { month: "Apr", income: 4100, expenses: 2400, profit: 1700 },
  { month: "May", income: 4500, expenses: 3000, profit: 1500 },
  { month: "Jun", income: 4700, expenses: 3200, profit: 1500 },
  { month: "Jul", income: 4600, expenses: 3100, profit: 1500 },
  { month: "Aug", income: 4300, expenses: 2800, profit: 1500 },
  { month: "Sep", income: 4200, expenses: 2900, profit: 1300 },
  { month: "Oct", income: 4400, expenses: 3100, profit: 1300 },
  { month: "Nov", income: 4600, expenses: 3300, profit: 1300 },
  { month: "Dec", income: 4800, expenses: 3500, profit: 1300 },
];

const IncomeExpenseChart = () => {
  return (
    <div style={{ height: "300px", width: "100%", position: "relative" }}>
      {/* Bar Chart - Income & Expenses */}
      <div style={{ position: "absolute", width: "100%", height: "100%" }}>
        <ResponsiveBar
          data={data}
          keys={["income", "expenses"]}
          indexBy="month"
          margin={{ top: 50, right: 0, bottom: 50, left: 50 }}
          colors={["#fed9a6", "#decbe4"]}
          axisBottom={{
            legend: "Month",
            legendPosition: "middle",
            legendOffset: 36,
          }}
          axisLeft={{
            legend: "Amount ($)",
            legendPosition: "middle",
            legendOffset: -45,
          }}
          labelPosition="end"
          labelOffset={-16}
          labelTextColor={{ from: "color", modifiers: [["darker", 1.6]] }}
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
          barAriaLabel={(e) => `${e.id}: $${e.formattedValue} in ${e.indexValue}`}
        />
      </div>

      {/* Line Chart - Profit Overlay */}
      <div style={{ position: "absolute", width: "100%", height: "100%" }}>
        <ResponsiveLine
                  data={[
                    {
                      id: "expense",
                      data: data.map((d) => ({ x: d.month, y: d.expenses })),
                    },
                    {
                      id: "income",
                      data: data.map((d) => ({ x: d.month, y: d.income })),
                    },

                  ]}
                  margin={{ top: 50, right: 0, bottom: 50, left: 50 }}
                  xScale={{ type: "point" }}
                  yScale={{ type: "linear", min: "auto", max: "auto", stacked: false }}
                  axisBottom={null} // Hide duplicate X-axis
                  axisLeft={null} // Hide duplicate Y-axis
                  colors={{ scheme: "pastel1" }}
                  lineWidth={3}
                  pointSize={8}
                  pointColor={{ theme: "background" }}
                  pointBorderWidth={2}
                  pointBorderColor={{ from: "serieColor" }}
                  areaOpacity={0.1}
                  useMesh={true}
                  layers={['markers', 'axes', 'areas', 'crosshair', 'lines', 'points', 'slices', 'mesh', 'legends']}
                />
      </div>
    </div>
  );
};

export default IncomeExpenseChart;
