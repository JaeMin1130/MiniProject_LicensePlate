import { Alert, Box, Collapse, IconButton, ListItemButton, ListItemText, Typography } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react"; 

export default function AlertNothing(props) {
  const navigate = useNavigate();

  useEffect(() => {
    if (props.open) {
      const timeoutId = setTimeout(() => {
        props.setOpen(false);
      }, 1000);

      return () => {
        clearTimeout(timeoutId);
      };
    }
  }, [props.open, props.setOpen]);

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
        </Box>
      </Alert>
    </Collapse>
  );
}