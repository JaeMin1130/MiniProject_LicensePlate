import * as React from "react";
import { styled } from "@mui/material/styles";
import { Tab, Tabs, Box } from "@mui/material";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const StyledTabs = styled(Tabs)({
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  flex: 1,

  flexDirection: "row",
  "& .MuiTabs-indicator": {
    backgroundColor: "transparent",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    height: "100%",
    "& .MuiTabs-indicatorSpan": {
      maxWidth: 60,
      width: "100%",
      backgroundColor: "#635ee7",
    },
  },
});

const StyledTab = styled(Tab)(({ theme }) => ({
  alignItems: "center",
  justifyContent: "center",
  display: "flex",
  textTransform: "none",
  fontWeight: "bold",
  fontSize: theme.typography.pxToRem(20),
  marginRight: theme.spacing(1),
  color: "rgba(255, 255, 255, 0.7)",
  "&.Mui-selected": {
    color: "#fff",
  },
  "&.Mui-focusVisible": {
    backgroundColor: "rgba(100, 95, 228, 0.32)",
  },
}));

const RightBox = styled(Tab)(({ theme }) => ({
  alignItems: "center",
  justifyContent: "center",
  display: "flex",
  textTransform: "none",
  fontWeight: "bold",
  fontSize: theme.typography.pxToRem(20),
  marginLeft: "auto",
  color: "rgba(255, 255, 255, 0.7)",
  "&.Mui-selected": {
    color: "#fff",
  },
  "&.Mui-focusVisible": {
    backgroundColor: "rgba(100, 95, 228, 0.32)",
  },
}));

export default function CustomizedTabs({ onLogin, isLoggedIn, onTabChange, onLogout }) {
  const [value, setValue] = useState(isLoggedIn ? 0 : 3);
  const navigate = useNavigate();

  const handleChange = (event, newValue) => {
    setValue(newValue);
    onTabChange(newValue);
  };

  const handleLogoutClick = () => {
    onTabChange(3);
    onLogout();
    // navigate('/main');
  };

  return (
    <Box sx={{ width: "100%" }}>
      <Box sx={{ bgcolor: "#2e1534", display: "flex", p: 3 }}>
        <StyledTabs value={value} onChange={handleChange} aria-label="styled tabs example">
          {!isLoggedIn
            ? [
                <StyledTab key="0" label="차량 입출입 현황" disabled />,
                <StyledTab key="1" label="검색" disabled />,
                <StyledTab key="2" label="차량등록" disabled />,
              ]
            : [
                <StyledTab
                  key="0"
                  label="차량 입출입 현황"
                  onClick={() => {
                    onTabChange(0);
                    navigate("MiniProject_LicensePlate/main/record");
                  }}
                />,
                <StyledTab
                  key="1"
                  label="검색"
                  onClick={() => {
                    onTabChange(1);
                    navigate("MiniProject_LicensePlate/main/search");
                  }}
                />,
                <StyledTab
                  key="2"
                  label="차량등록"
                  onClick={() => {
                    onTabChange(2);
                    navigate("MiniProject_LicensePlate/main/enroll");
                  }}
                />,
              ]}
          {!isLoggedIn ? (
            <RightBox key="3" label="로그인" onClick={onLogin} />
          ) : (
            <RightBox key="3" label="로그아웃" onClick={handleLogoutClick} />
          )}
        </StyledTabs>
      </Box>
    </Box>
  );
}
