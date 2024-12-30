import React, { useState } from 'react'
import DialogTitle from '@mui/material/DialogTitle';
import Dialog from '@mui/material/Dialog';
import Style from './HallDialog.module.css';
import { MenuItem, OutlinedInput, Select } from '@mui/material';
import { createHall } from '../../api';

export default function HallDialog({ open, setOpen, workspaceId }) {
  const Amenities = {
    'SCREEN': 'Screen',
    'PROJECTOR': 'Projector',
    'AC': 'AC',
    'CEILING_FANS': 'Ceiling Fans',
    'WHITE_BOARD': 'White Board'
  }

  const [hall, setHall] = useState({
    "capacity": null,
    "description": null,
    "pricePerHour": null,
    "aminities": []
  })

  return (
    <Dialog open={open}>
      <DialogTitle className={`bg-primary text-white`}>New Hall</DialogTitle>
      <hr/>
      <div className={`${Style['hall-dialog__content']} bg-primary`}>
        <label htmlFor="hall-name">Name:</label>
        <input type="text" id="hall-name" onChange={(e) => setHall({...hall, name: e.target.value})}/>
        <label htmlFor="hall-description">Description:</label>
        <textarea id="hall-description" onChange={(e) => setHall({...hall, description: e.target.value})}/>
        <label htmlFor="hall-rate">Price Per Hour:</label>
        <input type="number" id="hall-rate" onChange={(e) => setHall({...hall, pricePerHour: e.target.value})}/>
        <label htmlFor="hall-capacity">Capacity:</label>
        <input type="number" id="hall-capacity" onChange={(e) => setHall({...hall, capacity: e.target.value})}/>
        <label htmlFor="hall-aminities">Amenities:</label>
        <Select
          multiple
          id="hall-aminities-select"
          inputProps={{
            id: 'hall-aminities-select',
          }}
          value={hall.aminities}
          onChange={(e) => {console.log(e.target.value); setHall({...hall, aminities: e.target.value})}}
          input = {<OutlinedInput label="Select Amenities" sx={{backgroundColor: 'white'}}/>}
          >
            {Object.keys(Amenities).map((amenity, index) => (
              <MenuItem key={index} value={amenity}>{Amenities[amenity]}</MenuItem>
            ))}
        </Select>

        <div className={`${Style['hall-dialog__content__buttons']} flex justify-end gap-4`}>
          <button className={`bg-secondary1 text-white px-4 py-2 rounded-md`} onClick={()=>{setOpen(false)}}>Cancel</button>
          <button className={`bg-secondary1 text-white px-4 py-2 rounded-md`} onClick={()=>{createHall(workspaceId, hall); setOpen(false)}}>Add Hall</button>
        </div>
      </div>
    </Dialog>
  )
}
