import { Link } from 'react-router-dom';
import { Card } from 'react-bootstrap';
import axios from 'axios';
import { getCookie } from '../../utils/cookie';
import {
  ResourceCard,
  ResourceCardTitle,
  ResourceContent,
  ResourceOpion,
} from '../../styles/ResourceCard';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const ResourceItem = (props) => {
  const [resourceItem, setResourceItem] = useState('');
  const navigate = useNavigate();
  // const resourceNo = resource.resourceNo;
  const {
    able,
    content,
    resourceName,
    location,
    people,
    availavleTime,
    adminNo,
    option,
    fuel,
    createAt,
    modifyAt,
    path,
    cateNo,
    resourceNo,
  } = props.resource;

  const getResourceNo = async (resourceNo) => {
    axios
      .get(
        `${process.env.REACT_APP_SERVER_PORT}/resource/detail?resourceNo=${resourceNo}`,
        {
          headers: {
            Authorization: getCookie('accessToken'),
          },
        },
      )
      .then((response) => {
        setResourceItem(response.data.data);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  useEffect(() => {
    getResourceNo(resourceNo);
  }, []);

  const handleResourceView = (resourceNo) => {
    console.log(resourceNo);
    navigate('/admin/resourceupdate', {
      state: resourceNo,
    });
  };

  return (
    <ResourceCard
      onClick={() => {
        handleResourceView(resourceNo);
      }}
    >
      <Card>
        <Card.Img style={{ width: 'auto', height: '150px' }} src={path} />
        <Card.Body>
          <ResourceCardTitle>
            {resourceNo}. {resourceName}
          </ResourceCardTitle>
          <ResourceOpion>{option}</ResourceOpion>
          <ResourceContent>{content}</ResourceContent>
        </Card.Body>
      </Card>
    </ResourceCard>
  );
};

export default ResourceItem;
