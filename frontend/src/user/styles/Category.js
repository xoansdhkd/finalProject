import styled from 'styled-components';

const AllContainer = styled.div`
  font-family: NanumGothicBold;
  margin-bottom: 70px;
`;

const Container = styled.div`
  background-color: #f6f6f6;
  display: flex;
  justify-content: space-around;
  align-items: center;
  border-radius: 12px;
  box-shadow: rgb(0 0 0 / 12%) 0px 6px 16px;
  margin-top: 20px;
  width: 1075px;
  height: 300px;
`;

const TitleContainer = styled.div`
  padding-left: 30px;
  font-size: 20px;
  font-weight: bolder;
`;

const ImageContainer = styled.img`
  width: 300px;
  height: 240px;
  padding: 10px;
  border-radius: 20px;
  box-shadow: rgb(0 0 0 / 12%) 0px 6px 16px;
`;

export { AllContainer, Container, ImageContainer, TitleContainer };
