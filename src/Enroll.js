import * as React from 'react';
import { Box, Typography, Button, TextField } from "@mui/material";

import { useState, useEffect } from 'react';
import ImageLoader from './ImageLoader';

export default function Enroll() {
    const [carNumber, setCarNumber] = useState("");

    const handleCarNumberChange = (event) => {
        setCarNumber(event.target.value);
    };

    const handleRegister = () => {
    };

    return (
        <Box sx={{ margin: "20px" }}>
            <Box
                sx={{
                    height: '100vh',
                    width: '100%',
                    display: "flex",
                    alignItems: "center",
                    flexDirection: "row",
                    gap: '20px',
                }}
            >
                <Box
                    sx={{
                        width: '50%',
                        height: '100vh',
                        border: "1px solid rgb(189, 188, 188)",
                        paddingBlock: "10px",
                        justifyContent: "space-evenly",
                        flexDirection: 'column',
                        display: 'flex',
                    }}
                >

                    <Box>
                        <ImageLoader />
                    </Box>
                    <Box
                        sx={{
                            display: "flex",
                            flexDirection: "row",
                            alignItems: "center",
                            justifyContent: "center",
                            gap: 2,
                            flex: 1,
                        }}
                    >

                        <TextField
                            label="차량번호를 입력하세요"
                            variant="outlined"
                            value={carNumber}
                            onChange={handleCarNumberChange}
                            sx={{ width: 300, mb: 2 }}
                        />
                        <Button sx={{
                            mb: 2, bgcolor: "#CCCCCC", '&:hover': {
                                bgcolor: "#635ee7",
                            }, color: 'black', border: '1px solid black'
                        }} variant="contained" color="primary" onClick={handleRegister}>
                            등록
                        </Button>
                    </Box>
                </Box>

                <Box
                    sx={{
                        width: '70%',
                        height: '100vh',
                        border: "1px solid rgb(189, 188, 188)",
                        paddingBlock: "10px",

                    }}
                >
                    <Typography variant="h4">DB 등록 확인</Typography>
                </Box>
            </Box>
        </Box>
    );
}
