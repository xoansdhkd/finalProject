import React, { useState, useRef } from 'react';
import {
  SbContainer,
  SbUl,
  SbLi,
  SbChildUl,
  SbChildLi,
} from '../styles/Sidebar';
import { Link } from 'react-router-dom';
import {
  BsFillPersonFill,
  BsReverseLayoutTextSidebarReverse,
  BsCalendar4Week,
  BsArrowReturnRight,
} from 'react-icons/bs';

import { BiDownArrow } from 'react-icons/bi';

function Sidebar() {
  const [reservation, setReservation] = useState(false);
  const toggleInfo = useRef();

  const changeTab = () => {
    if (reservation) {
      setReservation(false);
      toggleInfo.current.style.display = 'block';
    } else {
      setReservation(true);
      toggleInfo.current.style.display = 'none';
    }
    // console.log(reservation);
  };

  const floatRight = {
    float: 'right',
    marginTop: '5px',
  };

  return (
    <SbContainer>
      <SbUl>
        <Link
          to="/admin/main"
          style={{ textDecoration: 'none', color: 'black' }}
        >
          <SbLi>
            <BsFillPersonFill /> 사용자관리
          </SbLi>
        </Link>
        <Link
          to="/admin/resource"
          style={{ textDecoration: 'none', color: 'black' }}
        >
          <SbLi>
            <BsReverseLayoutTextSidebarReverse /> 자원관리
          </SbLi>
        </Link>
        <SbLi onClick={changeTab}>
          <BsCalendar4Week /> 예약관리 <BiDownArrow style={floatRight} />
        </SbLi>
        <SbChildUl ref={toggleInfo}>
          <SbChildLi>
            <Link to="/admin/registerbook">
              <BsArrowReturnRight />
              &nbsp;예약등록
            </Link>
          </SbChildLi>
          <SbChildLi>
            <Link to="/admin/employeebook">
              <BsArrowReturnRight />
              &nbsp;사용자별 예약관리
            </Link>
          </SbChildLi>
          <SbChildLi>
            <Link to="/admin/resourcelist">
              <BsArrowReturnRight />
              &nbsp;자원별 예약관리
            </Link>
          </SbChildLi>
        </SbChildUl>
      </SbUl>
    </SbContainer>
  );
}

export default Sidebar;
