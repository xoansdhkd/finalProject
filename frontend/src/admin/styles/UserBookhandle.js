import styled from 'styled-components';

const AllContainer = styled.div`
  font-family: NanumGothicBold;
`;

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  width: 80%;
  margin: auto;
`;

const HeadContainer = styled.div`
  font-size: 30px;
  font-weight: 600;
  padding-top: 70px;
  border-bottom: 4px solid black;
  margin-left: 100px;
`;

const BookContainer = styled.div`
  width: 800px;
  margin-top: 30px;
  margin-left: 18%;
  background-color: #f6f6f6;
  border-radius: 15px;
  opacity: 0.9;
`;

const NameContainer = styled.h1`
  margin-top: 5px;
  margin-left: 30px;
  padding-top: 15px;
  font-size: 22px;
  font-weight: 800;
  display: flex;
  justify-content: space-between;
`;

const CategoryContainer = styled.span`
  background-color: blue;
  color: white;
  font-size: 14px;
  padding: 7px;
  border-radius: 20px;
  margin-right: 15px;
`;

const ContentContainer = styled.div`
  margin-top: 30px;
  padding-bottom: 20px;
  font-size: 20px;
  font-weight: 800;
`;

const ContentSort = styled.div`
  margin-top: 10px;
  margin-left: 40px;
  display: flex;
  justify-content: flex-start;
  label {
    min-width: 150px;
  }
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  margin-right: 20px;
`;

const MagnifyingGlass = styled.div`
  button {
    border: none;
  }
`;

export {
  AllContainer,
  Container,
  HeadContainer,
  CategoryContainer,
  ContentContainer,
  BookContainer,
  NameContainer,
  ContentSort,
  ButtonContainer,
  MagnifyingGlass,
};
