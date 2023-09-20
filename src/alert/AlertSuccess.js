import { Alert, Box, Collapse, IconButton, ListItemButton, ListItemText, Typography } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { useNavigate } from "react-router-dom";

export default function AlertSuccess(props) {
  const navigate = useNavigate();

  return (
    <Collapse in={props.open}>
      <Alert
        action={
          <IconButton aria-label="close" color="error" size="small">
            <CloseIcon
              fontSize="inherit"
              onClick={() => {
                props.setOpen(false);
              }}
            />
          </IconButton>
        }
        severity="info"
      >
        <Box>
          <Typography>{props.text}</Typography>
          <ListItemButton sx={{ p: 0 }} onClick={() => navigate("/MiniProject_LicensePlate")}>
            <ListItemText primary="로그인 페이지로 이동하기"></ListItemText>
          </ListItemButton>
        </Box>
      </Alert>
    </Collapse>
  );
}
