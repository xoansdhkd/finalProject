import { useEffect, useState } from 'react';
import axios from 'axios';

import { Row, Col, Button } from 'react-bootstrap';

import ResourceItem from './ResourceItem';

import { ResourceCardUI, Container } from '../../styles/Resource';

const ResourceCard = ({ selected }) => {
  const [resources, setResources] = useState([]);

  const getAll = async () => {
    axios
      .get(`${process.env.REACT_APP_SERVER_PORT}/resource`)
      .then((response) => {
        setResources(response.data.data);
        console.log(resources);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const getSelect = async () => {
    axios
      .get(`${process.env.REACT_APP_SERVER_PORT}/resource/${selected}`)
      .then((response) => {
        setResources(response.data.data);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const getBookmark = async () => {
    axios
      .get(`${process.env.REACT_APP_SERVER_PORT}/resource/bookmark`)
      .then((response) => {
        setResources(response.data.data);
      })
      .catch((error) => {
        console.log(error);
      });
  };
  useEffect(() => {
    if (selected == '0') {
      getAll();
    } else if (selected == '4') {
      getBookmark();
    } else {
      getSelect();
    }

    console.log(resources);
  }, [selected]);

  useEffect(() => {}, [resources]);

  return (
    <Container>
      <ResourceCardUI>
        <Row style={{ width: '1000px' }}>
          {resources.map((resource, idx) => (
            <Col sm={3} key={idx} style={{ marginTop: '30px' }}>
              <ResourceItem resource={resource} />
            </Col>
          ))}
        </Row>
      </ResourceCardUI>
    </Container>
  );
};

export default ResourceCard;
