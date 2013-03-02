//
//  SettingsTableViewController.m
//  TwinRinksAdultHockey
//
//  Created by Andrew on 3/2/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import "SettingsTableViewController.h"
#import "Team.h"
#import "MemoryManager.h"

@interface SettingsTableViewController ()

@property (strong, nonatomic) NSString * lastSelectedTeam;

@end

@implementation SettingsTableViewController

@synthesize yourTeamsArray;

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    MemoryManager *myManager = [[MemoryManager alloc] init];
    yourTeamsArray = [myManager getYourTeamArray];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return yourTeamsArray.count;
}

-(NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return nil;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellIdentifier = @"yourTeamsCell";
    UITableViewCell *cell;
    cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }
    
    UILabel *label = (UILabel *)[cell viewWithTag:0];
    
    Team *temp = ((Team *)[yourTeamsArray objectAtIndex:indexPath.row]);
    
    label.text = [temp toString];
    return cell;
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *toAppend = [((Team *) [yourTeamsArray objectAtIndex:indexPath.row]) toString];
    self.lastSelectedTeam = toAppend;
    
    NSString *message = [@"Do you want to remove this team from your teams: " stringByAppendingString:toAppend];
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Remove?" message:message delegate:self cancelButtonTitle:@"No" otherButtonTitles:@"Yes", nil];
    [alert show];
}

- (void)alertView:(UIAlertView *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (buttonIndex == 1)
    {
        NSMutableArray *mutable = [NSMutableArray arrayWithArray:yourTeamsArray];
        for(int i=mutable.count -1;i>=0;i--) {
            Team *temp = [mutable objectAtIndex:i];
            if([[temp toString] isEqualToString:self.lastSelectedTeam])
               [mutable removeObjectAtIndex:i];
        }
        MemoryManager *myManager = [[MemoryManager alloc]init];
        [myManager saveYourTeamsArray:[NSArray arrayWithArray:mutable]];
        
        yourTeamsArray = [myManager getYourTeamArray];
        [self.tableView reloadData];
    }
}

@end
