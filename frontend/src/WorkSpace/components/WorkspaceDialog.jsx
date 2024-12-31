import { useState, useEffect } from "react";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import { useParams } from "react-router-dom";
import { TimePicker } from "@mui/x-date-pickers/TimePicker"; // MUI v5
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns"; // Adapter for date-fns
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { format } from "date-fns";
import { renderTimeViewClock } from "@mui/x-date-pickers/timeViewRenderers"; // For clock view

const WorkspaceDialog = ({
    open,
    onClose,
    workspaceData,
    workdays,
    onSave,
}) => {
    const { workspaceId } = useParams();
    const [workspaceName, setWorkspaceName] = useState(
        workspaceData.name || ""
    );
    const [workspaceDescription, setWorkspaceDescription] = useState(
        workspaceData.description || ""
    );
    const [workspaceLocation, setWorkspaceLocation] = useState(
        workspaceData.location || {}
    );
    const [workspaceWorkdays, setWorkspaceWorkdays] = useState(workdays || []);
    const [validationErrors, setValidationErrors] = useState({});

    // Define the full set of days in the week
    const allDays = [
        "SATURDAY",
        "SUNDAY",
        "MONDAY",
        "TUESDAY",
        "WEDNESDAY",
        "THURSDAY",
        "FRIDAY",
    ];

    // Initialize the state for workdays with default start and end times
    useEffect(() => {
        const updatedWorkdays = allDays.map((day) => {
            const existingDay = workdays.find((wd) => wd.weekDay === day);
            if (existingDay) {
                return existingDay; // Keep existing data
            }
            return {
                weekDay: day,
                startTime: "00:00:00",
                endTime: "00:00:00",
            };
        });
        setWorkspaceWorkdays(updatedWorkdays);
        setValidationErrors({});
    }, [workdays]);

    const handleSave = async () => {
        const errors = {};

        // Validate if any fields are empty
        if (!workspaceName)
            errors.workspaceName = "Workspace name is required.";
        if (!workspaceDescription)
            errors.workspaceDescription = "Description is required.";
        if (!workspaceLocation.departmentNumber)
            errors.departmentNumber = "Department number is required.";
        if (!workspaceLocation.street) errors.street = "Street is required.";
        if (!workspaceLocation.city) errors.city = "City is required.";

        // Validate the start and end times for each workday
        const violatedDays = workspaceWorkdays
            .filter((wd) => {
                const startTime = new Date(`1970-01-01T${wd.startTime}`);
                const endTime = new Date(`1970-01-01T${wd.endTime}`);
                return startTime > endTime;
            })
            .map((wd) => wd.weekDay);

        if (violatedDays.length > 0) {
            errors.time = `Start time must be earlier than end time for: ${violatedDays.join(
                ", "
            )}.`;
        }

        // If there are any errors, set the validationErrors state
        if (Object.keys(errors).length > 0) {
            setValidationErrors(errors);
            return; // Don't proceed if validation fails
        }

        // Clear validation errors
        setValidationErrors({});

        // Proceed with save
        const updatedWorkspace = {
            ...workspaceData,
            name: workspaceName,
            description: workspaceDescription,
            location: workspaceLocation,
        };

        const updatedWorkdays = workspaceWorkdays.filter(
            (wd) => wd.startTime !== wd.endTime
        );
        onSave(updatedWorkspace, updatedWorkdays);
    };

    const handleLocationChange = (e) => {
        const { name, value } = e.target;
        setWorkspaceLocation((prevLocation) => ({
            ...prevLocation,
            [name]: value,
        }));
    };

    const handleTimeChange = (day, type, value) => {
        // Convert the value to 24-hour format time (HH:mm:ss) for consistent handling
        const formattedTime = value ? format(value, "HH:mm:ss") : "00:00:00";
        setWorkspaceWorkdays((prevWorkdays) =>
            prevWorkdays.map((wd) =>
                wd.weekDay === day ? { ...wd, [type]: formattedTime } : wd
            )
        );
    };

    const textFieldStyles = {
        marginTop: "0.5rem",
        marginBottom: "0.5rem",
        padding: "0.5rem",
        borderRadius: "5px",
        "& .MuiInputLabel-root": {
            color: "white",
        },
        "& .MuiOutlinedInput-root": {
            "& fieldset": {
                borderColor: "white",
            },
            "&:hover fieldset": {
                borderColor: "white",
            },
            "&.Mui-focused fieldset": {
                borderColor: "white",
                borderWidth: "3px",
            },
            "& input": {
                color: "white",
            },
            "& textarea": {
                color: "white",
            },
            "& svg": {
                fill: "white",
            },
        },
    };

    return (
        <Dialog
            open={open}
            onClose={onClose}
            maxWidth="md"
            fullWidth
            sx={{
                "& .MuiDialog-paper": {
                    border: "2px solid white",
                    borderRadius: "10px",
                    color: "white",
                },
            }}
        >
            <DialogTitle
                className="bg-primary"
                sx={{
                    fontSize: "1.25rem",
                    fontWeight: "bold",
                    paddingBottom: "0.5rem",
                }}
            >
                Edit Workspace
            </DialogTitle>
            <DialogContent
                className="bg-primary"
                sx={{ paddingBottom: "1.5rem" }}
            >
                <div className="mb-4">
                    <TextField
                        label="Workspace Name"
                        variant="outlined"
                        fullWidth
                        value={workspaceName}
                        onChange={(e) => setWorkspaceName(e.target.value)}
                        className="mb-4"
                        sx={textFieldStyles}
                        error={!!validationErrors.workspaceName}
                        helperText={validationErrors.workspaceName}
                        inputProps={{ maxLength: 50 }}
                    />
                </div>

                <div className="mb-4">
                    <TextField
                        label="Description"
                        variant="outlined"
                        fullWidth
                        multiline
                        rows={4}
                        value={workspaceDescription}
                        onChange={(e) =>
                            setWorkspaceDescription(e.target.value)
                        }
                        sx={textFieldStyles}
                        error={!!validationErrors.workspaceDescription}
                        helperText={validationErrors.workspaceDescription}
                        inputProps={{ maxLength: 500 }}
                    />
                </div>

                {/* Location Section */}
                <div className="mb-4">
                    <h3 style={{ fontSize: "1.25rem", marginBottom: "0.5rem" }}>
                        Location
                    </h3>
                    <div style={{ display: "flex", gap: "1rem" }}>
                        <TextField
                            label="Department Number"
                            variant="outlined"
                            fullWidth
                            name="departmentNumber"
                            value={workspaceLocation.departmentNumber || ""}
                            onChange={handleLocationChange}
                            onInput={(e) => {
                                e.target.value = e.target.value.replace(
                                    /[^0-9]/g,
                                    ""
                                );
                            }}
                            sx={textFieldStyles}
                            inputProps={{
                                pattern: "[0-9]*",
                                inputMode: "numeric",
                                min: 0,
                            }}
                            type="number"
                            error={!!validationErrors.departmentNumber}
                            helperText={validationErrors.departmentNumber}
                        />
                        <TextField
                            label="Street"
                            variant="outlined"
                            fullWidth
                            name="street"
                            value={workspaceLocation.street || ""}
                            onChange={handleLocationChange}
                            sx={textFieldStyles}
                            error={!!validationErrors.street}
                            helperText={validationErrors.street}
                            inputProps={{ maxLength: 100 }}
                        />
                        <TextField
                            label="City"
                            variant="outlined"
                            fullWidth
                            name="city"
                            value={workspaceLocation.city || ""}
                            onChange={handleLocationChange}
                            sx={textFieldStyles}
                            error={!!validationErrors.city}
                            helperText={validationErrors.city}
                            inputProps={{ maxLength: 50 }}
                        />
                    </div>
                </div>

                {/* Workdays Section */}
                <div>
                    <h3 style={{ fontSize: "1.25rem", marginBottom: "0.5rem" }}>
                        Workdays
                    </h3>
                    {Object.keys(validationErrors).length > 0 && (
                        <div
                            style={{
                                color: "red",
                                fontSize: "0.875rem",
                                marginBottom: "1rem",
                            }}
                        >
                            {validationErrors.time && (
                                <p
                                    dangerouslySetInnerHTML={{
                                        __html: validationErrors.time,
                                    }}
                                />
                            )}
                        </div>
                    )}
                    {workspaceWorkdays.map((day) => (
                        <div key={day.weekDay} style={{ marginBottom: "0" }}>
                            <div
                                style={{
                                    display: "flex",
                                    alignItems: "center",
                                    gap: "1rem",
                                }}
                            >
                                <h4
                                    style={{
                                        width: "8rem",
                                        marginLeft: "3rem",
                                        color:
                                            validationErrors.time &&
                                            validationErrors.time.includes(
                                                day.weekDay
                                            )
                                                ? "red"
                                                : "white",
                                    }}
                                >
                                    {day.weekDay}
                                </h4>
                                <LocalizationProvider
                                    dateAdapter={AdapterDateFns}
                                >
                                    <TimePicker
                                        label={`Start Time`}
                                        value={
                                            new Date(
                                                `1970-01-01T${day.startTime}`
                                            )
                                        }
                                        onChange={(value) =>
                                            handleTimeChange(
                                                day.weekDay,
                                                "startTime",
                                                value
                                            )
                                        }
                                        renderInput={(props) => (
                                            <TextField
                                                {...props}
                                                sx={{
                                                    ...textFieldStyles,
                                                    width: "120px",
                                                }}
                                            />
                                        )}
                                        // ampm={false}
                                        viewRenderers={{
                                            hours: renderTimeViewClock,
                                            minutes: renderTimeViewClock,
                                            seconds: renderTimeViewClock,
                                        }}
                                        sx={textFieldStyles}
                                    />
                                    <TimePicker
                                        label={`End Time`}
                                        value={
                                            new Date(
                                                `1970-01-01T${day.endTime}`
                                            )
                                        }
                                        onChange={(value) =>
                                            handleTimeChange(
                                                day.weekDay,
                                                "endTime",
                                                value
                                            )
                                        }
                                        renderInput={(props) => (
                                            <TextField
                                                {...props}
                                                sx={{
                                                    ...textFieldStyles,
                                                    width: "120px",
                                                }}
                                            />
                                        )}
                                        // ampm={false}
                                        viewRenderers={{
                                            hours: renderTimeViewClock,
                                            minutes: renderTimeViewClock,
                                            seconds: renderTimeViewClock,
                                        }}
                                        sx={textFieldStyles}
                                    />
                                </LocalizationProvider>
                            </div>
                        </div>
                    ))}
                </div>
            </DialogContent>

            <DialogActions
                className="bg-primary"
                sx={{ padding: "1rem", justifyContent: "center" }}
            >
                <Button
                    onClick={onClose}
                    sx={{
                        padding: "0.75rem 1.5rem",
                        marginRight: "1rem",
                        fontWeight: "bold",
                        width: "6rem",
                        backgroundColor: "#d32f2f",
                        color: "white",
                        "&:hover": { backgroundColor: "#c62828" },
                    }}
                >
                    Cancel
                </Button>
                <Button
                    onClick={handleSave}
                    sx={{
                        padding: "0.75rem 1.5rem",
                        marginLeft: "1rem",
                        fontWeight: "bold",
                        width: "6rem",
                        backgroundColor: "#388e3c",
                        color: "white",
                        "&:hover": { backgroundColor: "#2c6e29" },
                    }}
                >
                    Save
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default WorkspaceDialog;
