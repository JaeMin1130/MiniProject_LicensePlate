import { Alert, Collapse, IconButton } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { useEffect } from "react"; 

export default function AlertError(props) {
  
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
        severity="error"
        sx={{ mb: 2, position: 'relative', zIndex: 1 }}
      >
        {props.text}
      </Alert>
    </Collapse>
  );
}
