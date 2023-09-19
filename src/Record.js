import * as React from "react";
import { Box } from "@mui/material";
import Images from "./Images";
import List from "./List";
import axios from "axios";
import { API_BASE_URL } from "./api/api-config";
import { useState, useEffect } from "react";
import { call } from "./api/ApiService";
import { format, parseISO } from "date-fns";
import dayjs from "dayjs";
import advancedFormat from "dayjs/plugin/advancedFormat";
import Predict from "./Predict";

dayjs.extend(advancedFormat);

export default function Record() {
  const [imageUpload, setImageUpload] = useState([]);
  const [startDate, setStartDate] = useState(null); // 시작날짜
  const [endDate, setEndDate] = useState(null); // 종료날짜
  const [rows, setRows] = useState([]); // 레코드(행) 목록
  const [rowSelectionModel, setRowSelectionModel] = useState([]);
  const [open, setOpen] = useState(false); // 검색결과 유무 팝업상태
  const [latestId, setLatestId] = useState(rows.length - 1);
  const [isLoading, setIsLoading] = useState(false);
  const [responseData, setResponseData] = useState(null);
  const [plateImage, setPlateImage] = useState(null);

  // rows 상태가 변경될 때마다 재랜더링 ([] <-종속성에 추가)
  useEffect(() => {

  }, [rows]);

  useEffect(() => {
    const today = dayjs();
    onQuerySubmit(today, today);
  }, []);

  // 3.차량 출입 로그 기록
  const showRecord = async (imageSrc) => {

    const formData = new FormData();

    formData.append("file", imageSrc);

    setIsLoading(true);

    try {
      const response = await axios.post(`${API_BASE_URL}/main/record`, formData, {
        headers: {
          Authorization: localStorage.getItem("ACCESS_TOKEN"),
          "Content-Type": "multipart/form-data",
        },
      });

      if (response.data.status === 500) {

      }

      const data = response.data.data[0];
      const plateImage = data.plateImage;
      const item = response.data.data[1];
      setResponseData(item);

      const today = new Date();
      const formattedDate = format(today, "yy-MM-dd HH:mm:ss");

      const newId = latestId + 1;
      setLatestId(newId);
      setPlateImage(plateImage);

      const newRecord = {
        id: data.logId,
        accuracy: data.accuracy,
        licensePlate: data.licensePlate,
        logId: data.logId,
        modelType: data.modelType,
        plateImage: data.plateImage,
        state: data.state,
        vehicleImage: data.vehicleImage,
        date: formattedDate,
      };

      setRows((prevRows) => [newRecord, ...prevRows]);
      setIsLoading(false);
    } catch (error) {
      if (error.response) {
        console.error("Error response data:", error.response.data);
        console.error("Error response status:", error.response.status);
      } else if (error.request) {
        console.error("No response received:", error.request);
      } else {
        console.error("Error setting up the request:", error.message);
      }
    }
  };

  // 4.날짜별 로그 조회
  const onQuerySubmit = async (startDate, endDate) => {

    const jsStartDate = startDate.toDate();
    const jsEndDate = endDate.toDate();

    const formattedStartDate = format(jsStartDate, "yy-MM-dd");
    const formattedEndDate = format(jsEndDate, "yy-MM-dd");

    call(`/main/search/date/${formattedStartDate}/${formattedEndDate}`, "GET", null)
      .then((data) => {
        const responseData = data.data;
      
        if (Array.isArray(responseData)) {
          const updatedRows = responseData
            .map((record, index) => {
              const formattedDate = format(parseISO(record.date), "yy-MM-dd HH:mm:ss");
              return { ...record, id: index + 1, date: formattedDate }; // 새 객체 생성, ... <- 확산 연산자
            })
            .reverse();

          setRows(updatedRows);
        } else {
          console.error("데이터가 배열이 아닙니다:", responseData.data);
          setOpen(true);
        }
      })
      .catch((error) => {
        console.error("데이터 가져오기 오류:", error);
      });
  };

  const onDateChange = (newStartDate, newEndDate) => {
    setStartDate(newStartDate);
    setEndDate(newEndDate);
    onQuerySubmit(newStartDate, newEndDate);
  };

  return (
    <Box sx={{ margin: "20px" }}>
      <Box
        sx={{
          height: "100vh",
          width: "100%",
          display: "flex",
          alignItems: "center",
          flexDirection: "row",
          alignItems: "stretch",
          justifyContent: "space-between",
          gap: "20px",
        }}
      >
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            width: "65%",
            height: "100vh",
            alignItems: "center",
            justifyContent: "space-between",
          }}
        >
          <Box
            sx={{
              width: "100%",
              height: "50h",
              border: "1px solid rgb(189, 188, 188)",
            }}
          >
            <Images setImageUpload={setImageUpload} showRecord={showRecord} />
          </Box>
          <Box
            sx={{
              width: "100%",
              height: "48vh",
              border: "1px solid rgb(189, 188, 188)",
            }}
          >
            <List
              isRecord={true}
              setRows={setRows}
              rows={rows}
              rowSelectionModel={rowSelectionModel}
              setRowSelectionModel={setRowSelectionModel}
              isLoading={isLoading}
            />
          </Box>
        </Box>
        <Box
          sx={{
            height: "100vh",
            width: "35%",
            border: "1px solid rgb(189, 188, 188)",
            display: "flex",

          }}
        >
          <Predict rows={rows} data={responseData} isLoading={isLoading} plateImage={plateImage} />
        </Box>
      </Box>
    </Box>
  );
}