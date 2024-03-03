package com.example.myshoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

data class ShoppingItem(val id : Int ,
                        var name : String ,
                        var quantity : Int ,
                        var isEditing : Boolean = false)



@Composable
fun onview(){
    var sitem by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showdialog by remember { mutableStateOf(false)}
    var itemName by remember{ mutableStateOf("")}
    var itemQuantity by remember{ mutableStateOf("1")}
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Button(onClick = { showdialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(13.dp),
            border = BorderStroke(2.dp , Color.Black),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow,
                contentColor = Color.Red
            )
        ) {


            Text(text = "Add Item")
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Bunun yüzünden buton itilecek
        ){
            items(sitem){
                item ->
                if(item.isEditing){
                    ShoppingItemEditor(item = item, onEditComplete ={
                        editedName , editedQuantity ->
                        sitem = sitem.map { it.copy(isEditing = false) }
                        val editedItem = sitem.find{it.id == item.id}
                        editedItem?.let {
                            it.name = editedName
                            it.quantity = editedQuantity
                        }
                    } )
                }
                else
                {
                   ShoppingListItem(
                       item = item ,
                       onEditClick = {
                                 sitem = sitem.map { it.copy(isEditing = it.id == item.id) }
                       },
                       onDeleteClick = {
                           sitem = sitem - item
                       })
                }
            }
        }
    }

    if(showdialog){
       AlertDialog(onDismissRequest = { showdialog = false },
                    confirmButton = {
                                    Row(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                        Button(onClick = {

                                         if(itemName.isNotBlank()){
                                             val itemss = ShoppingItem(id = sitem.size+1 , name = itemName , quantity = itemQuantity.toInt())
                                             sitem = sitem + itemss
                                             showdialog = false
                                             itemName = ""
                                         }

                                        }) {
                                            Text(text = "Add")
                                        }
                                        Button(onClick = { showdialog = false }) {
                                            Text(text = "Cancel")
                                        }
                                    }
                    },
                     title = { Text(text = "Add shoping Item")},
                     text = {
                         Column {
                             OutlinedTextField(value = itemName, onValueChange = {itemName = it} , singleLine = true , modifier = Modifier
                                 .fillMaxWidth()
                                 .padding(8.dp))
                             OutlinedTextField(value = itemQuantity, onValueChange = {itemQuantity = it} , singleLine = true , modifier = Modifier
                                 .fillMaxWidth()
                                 .padding(8.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                         }
                     }
       )
    }
}



@Composable
fun ShoppingItemEditor(item : ShoppingItem , onEditComplete : (String , Int)->Unit)
{
    var editedName by remember { mutableStateOf(item.name)};
    var editedQuantity by remember { mutableStateOf(item.quantity.toString())}
    var isEditing by remember { mutableStateOf(item.isEditing)}

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
        )
    {
        BasicTextField(value = editedName, onValueChange = {
            editedName = it },
            singleLine = true,
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp)
            )
        BasicTextField(value = editedQuantity, onValueChange = {
            editedQuantity = it },
            singleLine = true,
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp)
        )
        Button(onClick = { isEditing = false
            onEditComplete(editedName , editedQuantity.toIntOrNull() ?: 1)}) {
            Text(text = "Save")
        }
    }
}


@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit

    )
{
    Row(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .border(
            border = BorderStroke(2.dp, Color(0XFF018786)),
            shape = RoundedCornerShape(20)
        ))
    {
        Text(text = item.name , modifier = Modifier.padding(8.dp))
        Text(text = "QTY : ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}