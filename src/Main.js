import { Box } from "@mui/material";
import CustomizedTabs from "./CustomizedTabs.js";
import Login from "./Login.js";
import Record from "./Record.js";
import Search from "./Search.js";
import Enroll from "./Enroll.js";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Main() {
  const [selectedTab, setSelectedTab] = useState(3);
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const navigate = useNavigate();

  const handleTabChange = (tabIndex) => {
    setSelectedTab(tabIndex);
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
    setSelectedTab(3);
    navigate("/MiniProject_LicensePlate");
  };

  const handleLogin = () => {
    setIsLoggedIn(true);
    handleTabChange(0);
    navigate("/MiniProject_LicensePlate/main");
    // handleTabChange(1);
    // setSelectedTab(1);
  };

  return (
    <Box sx={{ width: "100%", height: "100vh" }}>
      <CustomizedTabs
        isLoggedIn={isLoggedIn}
        onTabChange={handleTabChange}
        onLogin={handleLogin}
        onLogout={handleLogout}
      />
      {selectedTab === 0 && <Record />}
      {selectedTab === 1 && <Search selectedTab={selectedTab} />}
      {selectedTab === 2 && <Enroll />}
      {selectedTab === 3 && (
        <Login
          onLogin={handleLogin}
          onTabChange={handleTabChange}
          onLogout={handleLogout}
          isLoggedIn={isLoggedIn}
          setIsLoggedIn={setIsLoggedIn}
          selectedTab={selectedTab}
        />
      )}
    </Box>
  );
}
