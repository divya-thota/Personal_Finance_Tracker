import { ResponsiveSunburst } from "@nivo/sunburst";

function Total({ nodes, centerX, centerY, value }) {
  return (
    <text
      x={centerX}
      y={centerY}
      textAnchor="middle"
      dominantBaseline="central"
      style={{
        fontSize: "3vmin",
        fontWeight: 400,

      }}
    >
      $42004
      {value}
    </text>
  );
  }

const CategoryExpenseChart = () => {
  const data = {
    name: "Expenses",
    children: [
      {
        name: "Food",
        value: 10,
        children: [
          { name: "Groceries", value: 30 },
          { name: "Restaurants", value: 20 },
        ],
      },
      {
        name: "Transport",
        value: 150,
        children: [
          { name: "Gas", value: 100 },
          { name: "Public Transport", value: 50 },
        ],
      },
      {
        name: "Entertainment",
        value: 130,
        children: [
          { name: "Movies", value: 50 },
          { name: "Games", value: 80 },
        ],
      },
      {
        name: "Rent",
        value: 120,
      },
    ],
  };

  return (
    <ResponsiveSunburst
      data={data}
      id="name"
      value="value"
      cornerRadius={2}
      borderWidth={1}
      borderColor={{ from: "color", modifiers: [["brighter", 2.0]] }}
      colors={{ scheme: "pastel1" }}
      childColor={{ from: "color", modifiers: [["brighter", 0.4]] }}
      enableArcLabels={true}
      arcLabelsSkipAngle={10}
      arcLabel={(e) => "$ " + e.data.value}
      arcLabelsTextColor={{from: 'color',modifiers: [['darker',1.4]]}}
      layers={['arcs', 'arcLabels', Total]}
      tooltip={(e) => {
        return (
          <div
            style={{
              padding: 5,
              background: "#FFFFFF",
              boxShadow: `0px 7px 16px 0px ${
                e.color ? e.color : "rgba(0,0,0,0.27)"
              }`,
              display: "flex",
              gap: "5px",
              flexDirection: "column",
              alignItems: "center",
              justifyContent: "center",
              borderRadius: "10px",
              fontSize: "14px",
            }}
            className="max-w-[250px]"
          >
            {e.data.name}: ${e.data.value}
          </div>
        );
      }}
      style={{ height: "90%", width: "100%" }}
    />);
};

export default CategoryExpenseChart;
