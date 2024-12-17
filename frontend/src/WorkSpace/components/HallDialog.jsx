import React from 'react'
import DialogTitle from '@mui/material/DialogTitle';
import Dialog from '@mui/material/Dialog';
import Style from './HallDialog.module.css';

export default function HallDialog({ open, setOpen }) {
  return (
    <Dialog open={open}>
      <DialogTitle className={`bg-primary text-white`}>New Hall</DialogTitle>
      <hr/>
      <div className={`${Style['hall-dialog__content']} bg-primary`}>
        <label htmlFor="hall-name">Name:</label>
        <input type="text" id="hall-name" />
        <label htmlFor="hall-description">Description:</label>
        <textarea id="hall-description" />
        <label htmlFor="hall-image">Image:</label>
        <input type="file" id="hall-image" />
        <label htmlFor="hall-rate">Price Per Hour:</label>
        <input type="number" id="hall-rate" />
        <label htmlFor="hall-capacity">Capacity:</label>
        <input type="number" id="hall-capacity" />
        <div className={`${Style['hall-dialog__content__buttons']} flex justify-end gap-4`}>
          <button className={`bg-secondary1 text-white px-4 py-2 rounded-md`} onClick={()=>{setOpen(false)}}>Cancel</button>
          <button className={`bg-secondary1 text-white px-4 py-2 rounded-md`}>Add</button>
        </div>
      </div>
    </Dialog>
  )
}
