import { useState, useEffect } from 'react';
import axios from 'axios';

import { getCookie } from '../../utils/cookie';

import { VictoryBar, VictoryChart, VictoryAxis, VictoryTheme } from 'victory';

import { BarContainer, TitleContainer } from '../../styles/Bchart';

const BchartItem = ({ catenum }) => {
  const [title, setTitle] = useState('');
  const [data, setData] = useState([]);
  const [info, setInfo] = useState([]);

  const changeTitle = () => {
    if (catenum == 1 || catenum == '') {
      setTitle('회의실');
    } else if (catenum == 2) {
      setTitle('차량');
    } else if (catenum == 3) {
      setTitle('노트북');
    }
  };

  const getTest = () => {
    axios
      .get(
        `${process.env.REACT_APP_SERVER_PORT}/main/stickchart?cateNo=${catenum}`,
        {
          headers: {
            Authorization: getCookie('accessToken'),
          },
        },
      )
      .then((res) => {
        changeTitle();
        console.log(res.data.data);
        setData([res.data.data]);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  useEffect(() => {
    if (data.length > 0) {
      let result = [];
      const totalCount = data[0].totalCnt;
      const days = data[0].days;
      const counts = data[0].hourConference.map((v) => v.cnt);
      let temp2 = totalCount * days * 4; //분모
      let spliceCounts = [];
      for (let i = 0; i < counts.length; i += 4) {
        spliceCounts.push(
          counts[i] + counts[i + 1] + counts[i + 2] + counts[i + 3],
        );
      }

      for (let i = 1; i < 13; i++) {
        const tempResult = {
          quarter: i,
          earnings: spliceCounts[i - 1] / temp2,
        };
        result.push(tempResult);
      }

      setInfo(result);
    }
  }, [data]);

  useEffect(() => {
    getTest();
  }, [catenum]);

  return (
    <BarContainer>
      <TitleContainer>{title} 사용시간</TitleContainer>
      <VictoryChart
        width={1200}
        height={800}
        padding={{ left: 100 }}
        domainPadding={{ x: 50, y: 30 }}
        margin={200}
        theme={VictoryTheme.material}
        style={{
          axis: { stroke: 'white' },
          grid: { stroke: '#94A2AD' },
          tickLabels: { fontSize: 20, padding: 10 },
        }}
      >
        <VictoryAxis
          tickValues={[
            '08',
            '10',
            '12',
            '14',
            '16',
            '18',
            '20',
            '22',
            '00',
            '02',
            '04',
            '06',
          ]}
          tickFormat={(t) => `${t}`}
          style={{
            tickLabels: { fontSize: 25 },
          }}
        />
        <VictoryAxis
          dependentAxis
          tickValues={[0.2, 0.4, 0.6, 0.8, 1]}
          tickFormat={(x) => `${100 * x}%`}
          style={{
            tickLabels: { fontSize: 25 },
          }}
        />
        <VictoryBar
          data={info}
          x="quarter"
          y="earnings"
          barWidth={30}
          style={{
            data: { fill: 'darkblue' },
          }}
        />
      </VictoryChart>
    </BarContainer>
  );
};

export default BchartItem;
