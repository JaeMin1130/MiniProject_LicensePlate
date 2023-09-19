import * as React from 'react';
import { LocalizationProvider } from '@mui/x-date-pickers-pro';
import { AdapterDayjs } from '@mui/x-date-pickers-pro/AdapterDayjs';
import { DateRangePicker } from '@mui/x-date-pickers-pro/DateRangePicker';
import { Box, Button } from "@mui/material";
import dayjs from 'dayjs';
import advancedFormat from "dayjs/plugin/advancedFormat";
import { useState, useEffect } from "react";

dayjs.extend(advancedFormat);

export default function Calendar({ startDate, endDate, setStartDate, setEndDate, setRows, onDateChange, onQuerySubmit, selectedTab, }) {
    const [calendarDate, setCalendarDate] = React.useState(new Date());
    const [dateButtonText, setDateButtonText] = React.useState('');
    const [resetDateRange, setResetDateRange] = useState(true);

    const handleShortcutClick = (shortcut) => {
        const today = dayjs();
        const { getValue, label } = shortcut;

        const [newStartDate, newEndDate] = getValue();
        setStartDate(newStartDate);
        setEndDate(newEndDate);
        setCalendarDate(today);
        setDateButtonText(label);

        onQuerySubmit(newStartDate, newEndDate);
    };

    const shortcuts = [
        {
            label: '오늘',
            getValue: () => {
                const today = dayjs();
                return [today.startOf('day'), today.endOf('day')];
            },
        },
        {
            label: '1주',
            getValue: () => {
                const today = dayjs();
                const oneWeekAgo = today.subtract(1, 'week');
                return [oneWeekAgo, today];
            },
        },
        {
            label: '2주',
            getValue: () => {
                const today = dayjs();
                const twoWeeksAgo = today.subtract(2, 'week');
                return [twoWeeksAgo, today];
            },
        },
        {
            label: '1개월',
            getValue: () => {
                const today = dayjs();
                const oneMonthAgo = today.subtract(1, 'month');
                return [oneMonthAgo, today];
            },
        },
        {
            label: '6개월',
            getValue: () => {
                const today = dayjs();
                const sixMonthsAgo = today.subtract(6, 'month');
                return [sixMonthsAgo, today];
            },
        },
    ];

    const handleQueryButtonClick = () => {

        if (!startDate || !endDate) {
            return;
        }
        onQuerySubmit(startDate, endDate);
    };

    useEffect(() => {
        if (selectedTab === 1) {
            const today = dayjs();
            if (resetDateRange) {
                setStartDate(today);
                setEndDate(today);
                onQuerySubmit(today, today);
                handleQueryButtonClick() // 오늘 날짜로 데이터 조회
            }
            setResetDateRange(false);
        } else {
            setResetDateRange(true);
        }
    }, [selectedTab, resetDateRange]);

    return (
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <Box sx={{ gap: '8px', width: '100%', display: "flex" }}>
                {shortcuts.map((shortcut, index) => (
                    <Button
                        key={index}
                        onClick={() => handleShortcutClick(shortcut)}
                        variant="contained"
                        sx={{
                            width: '100%',
                            border: '1px solid black',
                            backgroundColor: '#DDDDDD',
                            color: 'black',
                            '&:hover': {
                                backgroundColor: '#CCCCCC',
                            },
                        }}
                    >
                        {shortcut.label}
                    </Button>
                ))}
            </Box>
            <p>조회날짜: {dateButtonText}</p>
            <DateRangePicker
                value={[startDate, endDate]}
                onChange={(newValue) => {
                    setStartDate(newValue[0]);
                    setEndDate(newValue[1]);
                    onDateChange(newValue[0], newValue[1]);
                }}
                calendarDate={calendarDate}
                localeText={{ start: '시작 날짜', end: '종료 날짜' }}
            />
            <Button
                onClick={handleQueryButtonClick}
                variant="contained"
                sx={{
                    mt: 3,
                    width: '100%',
                    border: '1px solid black',
                    backgroundColor: '#DDDDDD',
                    color: 'black',
                    '&:hover': {
                        backgroundColor: '#CCCCCC',
                    },
                }}
            >
                조회하기
            </Button>
        </LocalizationProvider>
    );
}